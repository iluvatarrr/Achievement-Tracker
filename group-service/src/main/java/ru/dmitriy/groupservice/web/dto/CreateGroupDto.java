package ru.dmitriy.groupservice.web.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record CreateGroupDto(
        @NotBlank(message = "Group title is required")
        @Size(min = 3, max = 100, message = "Title must be between 3 and 100 characters")
        String title,
        @Size(max = 1000, message = "Description cannot exceed 1000 characters")
        String description,
        @Size(max = 100, message = "Organisation name cannot exceed 100 characters")
        String organisation,
        @NotNull(message = "Public status is required")
        Boolean isPublic
) {}