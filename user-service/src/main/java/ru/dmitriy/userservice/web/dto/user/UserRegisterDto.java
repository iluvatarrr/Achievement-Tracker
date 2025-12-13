package ru.dmitriy.userservice.web.dto.user;

public record UserRegisterDto(
        String email,
        String password
) {
}
