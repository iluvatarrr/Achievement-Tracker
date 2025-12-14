package ru.dmitriy.goalservice.web.dto;

import jakarta.validation.constraints.*;
import ru.dmitriy.commondomain.domain.goal.GoalStatus;
import java.time.LocalDateTime;

public record UpdateSubGoalDto(
        @NotBlank(message = "SubGoal title can't be blank")
        @Size(min = 3, max = 255, message = "SubGoal title must be between 3 and 200 characters")
        String title,
        @Size(max = 255, message = "SubGoal description cannot exceed 1000 characters")
        String description,
        @NotNull(message = "SubGoal status can't be null")
        GoalStatus goalStatus,
        @NotNull(message = "SubGoal deadline is required")
        @Future(message = "SubGoal deadline must be in the future")
        LocalDateTime deadline) {
}
