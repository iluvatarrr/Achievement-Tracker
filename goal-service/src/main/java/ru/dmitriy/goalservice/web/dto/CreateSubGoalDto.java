package ru.dmitriy.goalservice.web.dto;

import java.time.LocalDateTime;

public record CreateSubGoalDto (
        Long id,
        String title,
        String description,
        LocalDateTime deadline) {
}