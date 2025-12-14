package ru.dmitriy.goalservice.web.dto;

import jakarta.validation.constraints.*;
import ru.dmitriy.commondomain.domain.goal.GoalCategory;
import ru.dmitriy.commondomain.domain.goal.GoalStatus;

import java.time.LocalDateTime;
import java.util.List;

public record UpdateGoalDto(
        @NotBlank(message = "Title can't be blank")
        @Size(min = 3, max = 255, message = "Title must be between 3 and 100 characters")
        String title,
        @Size(max = 255, message = "Description cannot exceed 1000 characters")
        String description,
        @NotNull(message = "GoalStatus can't be null")
        GoalStatus goalStatus,
        @NotNull(message = "GoalCategory can't be null")
        GoalCategory goalCategory,
        @NotNull(message = "Deadline is required")
        @Future(message = "Deadline must be in the future")
        LocalDateTime deadline,
        List<UpdateSubGoalDto> subGoalList) {
}
