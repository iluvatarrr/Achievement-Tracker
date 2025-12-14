package ru.dmitriy.goalservice.service.impl;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.dmitriy.commondomain.domain.exception.GoalNotFoundException;
import ru.dmitriy.commondomain.domain.exception.SubGoalNotFountException;
import ru.dmitriy.commondomain.domain.goal.Goal;
import ru.dmitriy.commondomain.domain.goal.GoalStatus;
import ru.dmitriy.commondomain.domain.goal.SubGoal;
import ru.dmitriy.goalservice.repository.SubGoalRepository;
import ru.dmitriy.goalservice.service.GoalService;
import ru.dmitriy.goalservice.service.SubGoalService;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class SubGoalServiceImpl implements SubGoalService {

    private final SubGoalRepository subGoalRepository;
    private final GoalService goalService;

    public SubGoalServiceImpl(SubGoalRepository subGoalRepository, GoalService goalService) {
        this.subGoalRepository = subGoalRepository;
        this.goalService = goalService;
    }

    @Override
    @Transactional(readOnly = true)
    public SubGoal getById(Long id) throws SubGoalNotFountException {
        return subGoalRepository.findById(id).orElseThrow(() ->
                new SubGoalNotFountException("Подцель не найдена, id: " + id));
    }

    @Override
    @Transactional(readOnly = true)
    public List<SubGoal> getAllByGoalId(Long goalId) throws GoalNotFoundException {
        Goal goal = goalService.getById(goalId);
        return goal.getSubGoalList();
    }

    @Override
    @Transactional
    public SubGoal update(Long subGoalId, SubGoal subGoalDto) throws SubGoalNotFountException {
        SubGoal subGoal = getById(subGoalId);
        subGoal.setTitle(subGoalDto.getTitle());
        subGoal.setDescription(subGoalDto.getDescription());
        subGoal.setGoalStatus(subGoalDto.getGoalStatus());
        goalService.calculateGoalProgress(subGoal.getGoal());
        subGoal.setDeadline(subGoalDto.getDeadline());
        return subGoalRepository.save(subGoal);
    }

    @Override
    @Transactional
    public SubGoal updateStatus(Long subGoalId, GoalStatus status) throws SubGoalNotFountException {
        SubGoal subGoal = getById(subGoalId);
        subGoal.setGoalStatus(status);
        if (status.equals(GoalStatus.DONE)) {
            subGoal.setCompletedAt(LocalDateTime.now());
        }
        goalService.calculateGoalProgress(subGoal.getGoal());
        return subGoalRepository.save(subGoal);
    }

    @Override
    @Transactional
    public void delete(Long subGoalId) throws SubGoalNotFountException {
        subGoalRepository.deleteById(subGoalId);
    }
}
