package ru.dmitriy.goalservice.web.controller;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import ru.dmitriy.commondomain.domain.exception.GoalNotFoundException;
import ru.dmitriy.commondomain.domain.exception.SubGoalNotFountException;
import ru.dmitriy.commondomain.domain.goal.GoalStatus;
import ru.dmitriy.goalservice.web.dto.SubGoalDto;
import ru.dmitriy.goalservice.web.dto.UpdateSubGoalDto;
import java.util.List;

public interface SubGoalController {
    List<SubGoalDto> getAllByGoalId(@Min(1) Long goalId) throws GoalNotFoundException;
    SubGoalDto updateStatus(@Min(1) Long subGoalId, @NotNull GoalStatus status) throws SubGoalNotFountException;
    void delete(@Min(1) Long subGoalId) throws SubGoalNotFountException;
    UpdateSubGoalDto update(@Min(1) Long subGoalId, @Valid UpdateSubGoalDto subGoalDto) throws SubGoalNotFountException;
}