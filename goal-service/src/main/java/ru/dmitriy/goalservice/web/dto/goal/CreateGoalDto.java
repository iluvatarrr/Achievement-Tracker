package ru.dmitriy.goalservice.web.dto.goal;

import jakarta.validation.constraints.*;
import ru.dmitriy.commondomain.domain.goal.GoalCategory;
import ru.dmitriy.commondomain.domain.goal.GoalStatus;
import ru.dmitriy.commondomain.domain.goal.GoalType;

import java.time.LocalDateTime;
import java.util.List;

public record CreateGoalDto(
        @NotBlank(message = "Title can't be blank")
        @Size(min = 3, max = 255, message = "Goal title must be between 3 and 255 characters")
        String title,
        @NotBlank(message = "Description can't be blank")
        @Size(min = 3, max = 255, message = "Goal description must be between 3 and 255 characters")
        String description,
        @NotNull(message = "GoalStatus can't be blank")
        GoalStatus goalStatus,
        @NotNull(message = "GoalStatus can't be blank")
        GoalType goalType,
        @NotNull(message = "GoalCategory can't be blank")
        GoalCategory goalCategory,
        @NotNull(message = "Deadline is required")
        @Future(message = "Deadline must be in the future")
        LocalDateTime deadline,
        List<CreateSubGoalDto> subGoalList) {
}
