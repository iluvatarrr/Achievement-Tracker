package ru.dmitriy.goalservice.service;

import ru.dmitriy.commondomain.domain.exception.GroupNotFoundException;
import ru.dmitriy.commondomain.domain.exception.SubGoalNotFountException;
import ru.dmitriy.commondomain.domain.exception.UserNotFoundException;
import ru.dmitriy.commondomain.domain.goal.Goal;
import ru.dmitriy.commondomain.domain.exception.GoalNotFoundException;
import ru.dmitriy.commondomain.domain.goal.GoalCategory;
import ru.dmitriy.commondomain.domain.goal.GoalStatus;
import ru.dmitriy.commondomain.domain.goal.SubGoal;

import javax.naming.ServiceUnavailableException;
import java.time.LocalDateTime;
import java.util.List;

public interface GoalService extends CRUDService<Long, Goal> {
    List<Goal> findAll();

    Goal getById(Long id) throws GoalNotFoundException;

    Long save(Goal goal, Long userId) throws UserNotFoundException, ServiceUnavailableException;

    Long save(Goal goal, Long userId, Long groupId) throws UserNotFoundException, ServiceUnavailableException;

    Goal addSubGoal(Long id, SubGoal subGoal) throws GoalNotFoundException;

    Goal removeSubGoal(Long id, Long subId) throws GoalNotFoundException, SubGoalNotFountException;

    List<Goal> findFiltered(GoalStatus status, GoalCategory category, LocalDateTime deadline, Long id) throws ServiceUnavailableException, UserNotFoundException;

    Goal updateGoalStatus(Long id, GoalStatus goalStatus) throws GoalNotFoundException;

    Goal update(Long id, Goal goal) throws GoalNotFoundException;

    void delete(Long id);

    void calculateGoalProgress(Goal goal);
}
