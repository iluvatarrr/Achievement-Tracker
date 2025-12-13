package ru.dmitriy.userservice.web.dto.auth;

public record JwtResponse(
        Long id,
        String username,
        String accessToken,
        String refreshToken
) { }
