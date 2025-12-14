package ru.dmitriy.goalservice.web.dto.goal;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.time.LocalDateTime;

public record CreateSubGoalDto (
        @NotBlank(message = "SubGoal title can't be blank")
        @Size(min = 3, max = 255, message = "SubGoal title must be between 3 and 2255 characters")
        String title,
        @Size(max = 255, message = "SubGoal description cannot exceed 255 characters")
        String description,
        @NotNull(message = "SubGoal deadline is required")
        @Future(message = "SubGoal deadline must be in the future")
        LocalDateTime deadline) {
}