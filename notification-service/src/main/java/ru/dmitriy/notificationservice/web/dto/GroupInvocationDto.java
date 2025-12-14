package ru.dmitriy.notificationservice.web.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import ru.dmitriy.commondomain.domain.notification.GroupInvitationStatus;

import java.time.LocalDateTime;

public record GroupInvocationDto(
        Long id,
        @NotBlank(message = "Group name is required")
        @Size(min = 2, max = 255, message = "Group name must be between 2 and 255 characters")
        String groupName,
        @NotBlank(message = "Username is required")
        @Size(min = 2, max = 255, message = "Username must be between 2 and 255 characters")
        String username,
        @NotBlank(message = "InvitedByName is required")
        @Size(min = 2, max = 255, message = "InvitedByName must be between 2 and 255 characters")
        String invitedByName,
        @NotNull(message = "Status is required")
        GroupInvitationStatus status,
        @NotNull(message = "CreatedAt is required")
        LocalDateTime createdAt,
        @NotNull(message = "CreatedAt is required")
        LocalDateTime expiresAt
) {}
