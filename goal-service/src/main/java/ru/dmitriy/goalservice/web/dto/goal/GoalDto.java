package ru.dmitriy.goalservice.web.dto.goal;

import ru.dmitriy.commondomain.domain.goal.GoalCategory;
import ru.dmitriy.commondomain.domain.goal.GoalStatus;
import ru.dmitriy.commondomain.domain.goal.GoalType;

import java.time.LocalDateTime;
import java.util.List;

public record GoalDto(
        Long id,
        String title,
        String description,
        GoalStatus goalStatus,
        GoalType goalType,
        GoalCategory goalCategory,
        Integer progressInPercent,
        LocalDateTime createdAt,
        LocalDateTime completedAt,
        LocalDateTime deadline,
        List<SubGoalDto> subGoalList) {
}
