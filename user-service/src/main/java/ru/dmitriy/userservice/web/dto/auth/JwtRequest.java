package ru.dmitriy.userservice.web.dto.auth;

import jakarta.validation.constraints.NotNull;

public record JwtRequest(
        @NotNull(message = "Username can`t be NULL")
        String email,
        @NotNull(message = "Password can`t be NULL")
        String password
) {
}
