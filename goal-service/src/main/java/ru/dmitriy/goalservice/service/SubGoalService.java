package ru.dmitriy.goalservice.service;

import ru.dmitriy.commondomain.domain.exception.GoalNotFoundException;
import ru.dmitriy.commondomain.domain.exception.SubGoalNotFountException;
import ru.dmitriy.commondomain.domain.goal.GoalStatus;
import ru.dmitriy.commondomain.domain.goal.SubGoal;
import java.util.List;

public interface SubGoalService {
    List<SubGoal> getAllByGoalId(Long goalId) throws GoalNotFoundException;
//    SubGoal create(Long goalId, SubGoal subGoal) throws GoalNotFoundException;
    SubGoal getById(Long id) throws SubGoalNotFountException;
    SubGoal update(Long subGoalId, SubGoal subGoalDto) throws SubGoalNotFountException;
    SubGoal updateStatus(Long subGoalId, GoalStatus status) throws SubGoalNotFountException;
    void delete(Long subGoalId) throws SubGoalNotFountException;
}