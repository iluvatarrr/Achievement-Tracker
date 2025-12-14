package ru.dmitriy.notificationservice.web.dto;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;

public record CreateGroupInvocationDto(
        @NotBlank(message = "Username can't be blank")
        String username,
        @NotNull(message = "Deadline is required")
        @Future(message = "Deadline must be in the future")
        LocalDateTime expiresAt
) {
}
