package ru.dmitriy.userservice.service.impl;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;
import ru.dmitriy.commondomain.domain.exception.UserNotFoundException;
import ru.dmitriy.commondomain.domain.user.User;
import ru.dmitriy.userservice.service.AuthService;
import ru.dmitriy.userservice.service.UserService;
import ru.dmitriy.userservice.web.dto.auth.JwtRequest;
import ru.dmitriy.userservice.web.dto.auth.JwtResponse;
import ru.dmitriy.userservice.web.security.JwtTokenProvider;

@Service
public class AuthServiceImpl implements AuthService {
    private final AuthenticationManager authenticationManager;
    private final UserService userService;
    private final JwtTokenProvider jwtTokenProvider;

    public AuthServiceImpl(AuthenticationManager authenticationManager, UserService userService, JwtTokenProvider jwtTokenProvider) {
        this.authenticationManager = authenticationManager;
        this.userService = userService;
        this.jwtTokenProvider = jwtTokenProvider;
    }

    @Override
    public JwtResponse login(JwtRequest loginRequest) throws UserNotFoundException {
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginRequest.email(), loginRequest.password()));
        User user = userService.getByUsername(loginRequest.email());
        return new JwtResponse(user.getId(),
                user.getUsername(),
                jwtTokenProvider.createAccessToken(user.getId(), user.getUsername(), user.getRoles()),
                jwtTokenProvider.createRefreshToken(user.getId(), user.getUsername()));
    }

    @Override
    public JwtResponse refresh(String refreshToken) throws UserNotFoundException {
        return jwtTokenProvider.refreshUserTokens(refreshToken);
    }

}
