package ru.dmitriy.userservice.service;

import ru.dmitriy.commondomain.domain.exception.UserNotFoundException;
import ru.dmitriy.userservice.web.dto.auth.JwtRequest;
import ru.dmitriy.userservice.web.dto.auth.JwtResponse;

public interface AuthService {
    JwtResponse login(JwtRequest loginRequest) throws UserNotFoundException;

    JwtResponse refresh(String refreshToken) throws UserNotFoundException;
}

