package ru.dmitriy.goalservice.web.dto;

import ru.dmitriy.commondomain.domain.goal.GoalStatus;

import java.time.LocalDateTime;

public record SubGoalDto(
    Long id,
    String title,
    String description,
    GoalStatus goalStatus,
    LocalDateTime createdAt,
    LocalDateTime completedAt,
    LocalDateTime deadline) {
}
