package ru.dmitriy.goalservice.service.impl;

import jakarta.persistence.EntityManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.dmitriy.commondomain.domain.exception.GroupNotFoundException;
import ru.dmitriy.commondomain.domain.exception.SubGoalNotFountException;
import ru.dmitriy.commondomain.domain.exception.UserNotFoundException;
import ru.dmitriy.commondomain.domain.goal.*;
import ru.dmitriy.commondomain.domain.group.Group;
import ru.dmitriy.commondomain.domain.user.User;
import ru.dmitriy.commondomain.domain.exception.GoalNotFoundException;
import ru.dmitriy.commondomain.listener.UserValidationResponseEventListener;
import ru.dmitriy.goalservice.repository.GoalRepository;
import ru.dmitriy.goalservice.repository.GoalSpecifications;
import ru.dmitriy.goalservice.service.GoalService;
import javax.naming.ServiceUnavailableException;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class GoalServiceImpl implements GoalService {

    private static final Logger log = LoggerFactory.getLogger(GoalServiceImpl.class);
    private final GoalRepository goalRepository;
    private final UserValidationResponseEventListener userValidationResponseEventListener;
    private final EntityManager entityManager;

    public GoalServiceImpl(GoalRepository goalRepository, UserValidationResponseEventListener userValidationResponseEventListener, EntityManager entityManager) {
        this.goalRepository = goalRepository;
        this.userValidationResponseEventListener = userValidationResponseEventListener;
        this.entityManager = entityManager;
    }

    @Override
    @Transactional(readOnly = true)
    public List<Goal> findAll() {
        return goalRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Goal getById(Long id) throws GoalNotFoundException {
        return goalRepository.findByIdWithSubGoals(id)
                .orElseThrow(()-> new GoalNotFoundException("Цель не найдена по id: " + id));
    }

    @Override
    @Transactional(readOnly = true)
    public List<Goal> findFiltered(GoalStatus status, GoalCategory category, LocalDateTime deadline, Long userId) throws ServiceUnavailableException, UserNotFoundException {
        userValidationResponseEventListener.checkUserId(userId);
        var spec = Specification.where(GoalSpecifications.fetchSubGoals())
                .and(GoalSpecifications.hasUserId(userId))
                .and(GoalSpecifications.hasStatus(status))
                .and(GoalSpecifications.hasCategory(category))
                .and(GoalSpecifications.deadlineBefore(deadline));
        return goalRepository.findAll(spec);
    }

    @Override
    @Transactional
    public Long save(Goal goal, Long userId) throws UserNotFoundException, ServiceUnavailableException {
        log.debug("Сохранение цели для пользователя: {}", userId);
        userValidationResponseEventListener.checkUserId(userId);
        User user = entityManager.getReference(User.class, userId);
        goal.setUser(user);
        log.info("Пользователь найден, сохраняем цель");
        goal.setCreatedAt(LocalDateTime.now());
        if (goal.getSubGoalList() != null) {
            for (var subGoals : goal.getSubGoalList()) {
                subGoals.setCreatedAt(LocalDateTime.now());
                subGoals.setGoalStatus(GoalStatus.IN_PROGRESS);
            }
        }
        goal.setGoalStatus(GoalStatus.IN_PROGRESS);
        calculateGoalProgress(goal);
         return goalRepository.save(goal).getId();
    }

    @Override
    public Long save(Goal goal, Long userId, Long groupId) throws UserNotFoundException, ServiceUnavailableException {
        log.debug("Сохранение цели для группы: {}", userId);
        userValidationResponseEventListener.checkUserId(userId);
        User user = entityManager.getReference(User.class, userId);
        checkUserAsMemberOfGroup(user, groupId);
        goal.setUser(user);
        var group = entityManager.getReference(Group.class, groupId);
        group.addGoal(goal);
        goal.setGroups(group);
        log.info("Пользователь найден, сохраняем цель");
        goal.setCreatedAt(LocalDateTime.now());
        if (goal.getSubGoalList() != null) {
            for (var subGoals : goal.getSubGoalList()) {
                subGoals.setCreatedAt(LocalDateTime.now());
                subGoals.setGoalStatus(GoalStatus.IN_PROGRESS);
            }
        }
        goal.setGoalStatus(GoalStatus.IN_PROGRESS);
        calculateGoalProgress(goal);
        return goalRepository.save(goal).getId();
    }

    public void checkUserAsMemberOfGroup(User user, Long groupId) throws UserNotFoundException {
        if (!user.getGroups().stream().map(Group::getId).toList().contains(groupId)) {
            throw new UserNotFoundException("Пользователь не найден как член группы");
        }
    }

    @Override
    @Transactional
    public Goal update(Long id, Goal goal) throws GoalNotFoundException {
        Goal existingGoal = getById(id);
        existingGoal.setCompletedAt(goal.getCompletedAt());
        existingGoal.setGoalCategory(goal.getGoalCategory());
        existingGoal.setGoalStatus(goal.getGoalStatus());
        existingGoal.setDescription(goal.getDescription());
        existingGoal.setDeadline(goal.getDeadline());
        existingGoal.setTitle(goal.getTitle());
        existingGoal.setSubGoalList(goal.getSubGoalList());
        calculateGoalProgress(existingGoal);
        return goalRepository.save(existingGoal);
    }

    @Override
    @Transactional
    public Goal addSubGoal(Long id, SubGoal subGoal) throws GoalNotFoundException {
        Goal existingGoal = getById(id);
        existingGoal.addSubGoal(subGoal);
        subGoal.setCreatedAt(LocalDateTime.now());
        subGoal.setGoalStatus(GoalStatus.getCreatedStatus());
        calculateGoalProgress(existingGoal);
        return goalRepository.save(existingGoal);
    }

    @Override
    @Transactional
    public Goal removeSubGoal(Long id, Long subId) throws GoalNotFoundException, SubGoalNotFountException {
        Goal existingGoal = getById(id);
        if (existingGoal.getSubGoalList() != null) {
            var subGoal = existingGoal.getSubGoalList()
                    .stream()
                    .filter(s -> s.getId().equals(subId))
                    .findAny()
                    .orElseThrow(() -> new SubGoalNotFountException("Подцель не найдена по id: " + subId));
            existingGoal.removeSubGoal(subGoal);
            calculateGoalProgress(existingGoal);
            return goalRepository.save(existingGoal);
        } else {
            throw new SubGoalNotFountException("Список подцелей пуст для цели: " + id);
        }
    }

    @Override
    @Transactional
    public Goal updateGoalStatus(Long id, GoalStatus goalStatus) throws GoalNotFoundException {
        Goal existingGoal = getById(id);
        existingGoal.setGoalStatus(goalStatus);
        if (goalStatus.equals(GoalStatus.DONE)) {
            existingGoal.setCompletedAt(LocalDateTime.now());
        }
        calculateGoalProgress(existingGoal);
        return existingGoal;
    }

    @Override
    @Transactional
    public void delete(Long id) {
        goalRepository.deleteById(id);
    }

    @Override
    public void calculateGoalProgress(Goal existingGoal) {
        var progressInPercent = 0;
        var subGoals = existingGoal.getSubGoalList();
        if (subGoals != null && !subGoals.isEmpty()) {
            var countOfDoneSubGoals = (int) existingGoal.getSubGoalList()
                    .stream()
                    .filter(s -> s.getGoalStatus()
                            .equals(GoalStatus.DONE))
                    .count();
            progressInPercent = countOfDoneSubGoals * 100 / subGoals.size();
        } else {
            progressInPercent = existingGoal.getGoalStatus().equals(GoalStatus.DONE) ? 100 : 0;
        }
        existingGoal.setProgressInPercent(progressInPercent);
    }
}
