package ru.dmitriy.userservice.web.controller;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import ru.dmitriy.commondomain.domain.exception.UserNotFoundException;
import ru.dmitriy.userservice.web.dto.auth.JwtRequest;
import ru.dmitriy.userservice.web.dto.auth.JwtResponse;
import ru.dmitriy.userservice.web.dto.user.UserRegisterDto;

public interface AuthController {
    JwtResponse login(@Valid JwtRequest jwtRequest) throws UserNotFoundException;
    UserRegisterDto register(@Valid UserRegisterDto userDto);
    JwtResponse refresh(@NotBlank String refreshToken) throws UserNotFoundException;
}
