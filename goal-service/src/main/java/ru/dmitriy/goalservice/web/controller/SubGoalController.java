package ru.dmitriy.goalservice.web.controller;

import ru.dmitriy.commondomain.domain.exception.GoalNotFoundException;
import ru.dmitriy.commondomain.domain.exception.SubGoalNotFountException;
import ru.dmitriy.commondomain.domain.goal.GoalStatus;
import ru.dmitriy.goalservice.web.dto.SubGoalDto;
import ru.dmitriy.goalservice.web.dto.UpdateSubGoalDto;

import java.util.List;

public interface SubGoalController {
    List<SubGoalDto> getAllByGoalId(Long goalId) throws GoalNotFoundException;
    SubGoalDto updateStatus(Long subGoalId, GoalStatus status) throws SubGoalNotFountException;
    void delete(Long subGoalId) throws SubGoalNotFountException;
    UpdateSubGoalDto update(Long subGoalId, UpdateSubGoalDto subGoalDto) throws SubGoalNotFountException;
}