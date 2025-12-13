package ru.dmitriy.userservice.web.controller;

import ru.dmitriy.commondomain.domain.exception.UserNotFoundException;
import ru.dmitriy.userservice.web.dto.auth.JwtRequest;
import ru.dmitriy.userservice.web.dto.auth.JwtResponse;
import ru.dmitriy.userservice.web.dto.user.UserRegisterDto;

public interface AuthController {
    JwtResponse login(JwtRequest jwtRequest) throws UserNotFoundException;
    UserRegisterDto register(UserRegisterDto userDto);
    JwtResponse refresh(String refreshToken) throws UserNotFoundException;
}
