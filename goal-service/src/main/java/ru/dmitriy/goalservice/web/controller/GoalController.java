package ru.dmitriy.goalservice.web.controller;

import ru.dmitriy.commondomain.domain.exception.GoalNotFoundException;
import ru.dmitriy.commondomain.domain.exception.ResourseNotFoundException;
import ru.dmitriy.commondomain.domain.exception.SubGoalNotFountException;
import ru.dmitriy.commondomain.domain.exception.UserNotFoundException;
import ru.dmitriy.commondomain.domain.goal.GoalCategory;
import ru.dmitriy.commondomain.domain.goal.GoalStatus;
import ru.dmitriy.goalservice.web.dto.CreateGoalDto;
import ru.dmitriy.goalservice.web.dto.CreateSubGoalDto;
import ru.dmitriy.goalservice.web.dto.GoalDto;
import ru.dmitriy.goalservice.web.dto.UpdateGoalDto;

import javax.naming.ServiceUnavailableException;
import java.time.LocalDateTime;
import java.util.List;

public interface GoalController {
    GoalDto getById(Long id) throws ResourseNotFoundException;
    Long create(CreateGoalDto dto, Long id) throws ResourseNotFoundException, ServiceUnavailableException;
    UpdateGoalDto update(Long id, UpdateGoalDto dto) throws ResourseNotFoundException;
    void delete(Long id);
    GoalDto addSubGoal(Long id, CreateSubGoalDto subGoalDto) throws GoalNotFoundException;
    GoalDto removeSubGoal(Long id, Long subId) throws GoalNotFoundException, SubGoalNotFountException;
    GoalDto updateStatusGoal(Long id, GoalStatus goalStatus) throws GoalNotFoundException;
    List<GoalDto> getAllFiltered(GoalStatus status, GoalCategory category,LocalDateTime deadline,Long id) throws UserNotFoundException, ServiceUnavailableException;
    Double calculateGoalProgress(Long goalId) throws GoalNotFoundException;
}
