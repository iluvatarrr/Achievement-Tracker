package ru.dmitriy.goalservice.web.controller;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import ru.dmitriy.commondomain.domain.exception.*;
import ru.dmitriy.commondomain.domain.goal.GoalCategory;
import ru.dmitriy.commondomain.domain.goal.GoalStatus;
import ru.dmitriy.goalservice.web.dto.goal.CreateGoalDto;
import ru.dmitriy.goalservice.web.dto.goal.CreateSubGoalDto;
import ru.dmitriy.goalservice.web.dto.goal.GoalDto;
import ru.dmitriy.goalservice.web.dto.goal.UpdateGoalDto;

import javax.naming.ServiceUnavailableException;
import java.time.LocalDateTime;
import java.util.List;

public interface GoalController {
    List<GoalDto> getAllByGroupId(@Min(1) Long groupId);
    GoalDto getById(@Min(1) Long id) throws ResourseNotFoundException;
    Long create(@Valid CreateGoalDto dto, @Min(1) Long id) throws ResourseNotFoundException, ServiceUnavailableException;
    UpdateGoalDto update(@Min(1) Long id, @Valid UpdateGoalDto dto) throws ResourseNotFoundException;
    void delete(@Min(1) Long id);
    GoalDto addSubGoal(@Min(1) Long id, @Valid CreateSubGoalDto subGoalDto) throws GoalNotFoundException;
    GoalDto removeSubGoal(@Min(1) Long id, @Min(1) Long subId) throws GoalNotFoundException, SubGoalNotFountException;
    GoalDto updateStatusGoal(@Min(1) Long id, @NotNull GoalStatus goalStatus) throws GoalNotFoundException;
    List<GoalDto> getAllFiltered( GoalStatus status, GoalCategory category, LocalDateTime deadline, @Min(1) Long id) throws UserNotFoundException, ServiceUnavailableException;
    Long createGroupGoal(@Valid @RequestBody CreateGoalDto goalDto, @Min(1) @RequestParam Long userId, @Min(1) @RequestParam Long groupId) throws UserNotFoundException, ServiceUnavailableException, GroupNotFoundException;
}
