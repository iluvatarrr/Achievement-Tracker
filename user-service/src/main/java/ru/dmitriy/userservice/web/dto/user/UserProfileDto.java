package ru.dmitriy.userservice.web.dto.user;

import jakarta.validation.constraints.NotBlank;

public record UserProfileDto(
        @NotBlank(message = "Firstname can't be blank")
        String firstName,
        @NotBlank(message = "Lastname can't be blank")
        String lastName) {
}
