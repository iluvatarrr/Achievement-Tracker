package ru.dmitriy.goalservice.web.dto;

import ru.dmitriy.commondomain.domain.goal.GoalCategory;
import ru.dmitriy.commondomain.domain.goal.GoalStatus;

import java.time.LocalDateTime;
import java.util.List;

public record UpdateGoalDto(
        Long id,
        String title,
        String description,
        GoalStatus goalStatus,
        GoalCategory goalCategory,
        LocalDateTime deadline,
        List<UpdateSubGoalDto> subGoalList) {
}
