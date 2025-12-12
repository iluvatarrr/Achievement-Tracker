package ru.dmitriy.goalservice.web.controller;

import ru.dmitriy.commondomain.domain.exception.GoalNotFoundException;
import ru.dmitriy.commondomain.domain.exception.SubGoalNotFountException;
import ru.dmitriy.commondomain.domain.goal.GoalStatus;
import ru.dmitriy.goalservice.web.dto.GoalDto;
import ru.dmitriy.goalservice.web.dto.SubGoalDto;

public interface GoalController extends CRUDController<Long, GoalDto> {
    GoalDto addSubGoal(Long id, SubGoalDto subGoalDto) throws GoalNotFoundException;
    GoalDto removeSubGoal(Long id, Long subId) throws GoalNotFoundException, SubGoalNotFountException;
    GoalDto updateStatusGoal(Long id, GoalStatus goalStatus) throws GoalNotFoundException;
}
