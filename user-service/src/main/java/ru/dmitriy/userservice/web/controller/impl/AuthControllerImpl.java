package ru.dmitriy.userservice.web.controller.impl;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.dmitriy.commondomain.domain.exception.UserNotFoundException;
import ru.dmitriy.commondomain.domain.user.User;
import ru.dmitriy.commondomain.util.Mappable;
import ru.dmitriy.commondomain.util.MapperRegistry;
import ru.dmitriy.userservice.service.AuthService;
import ru.dmitriy.userservice.service.UserService;
import ru.dmitriy.userservice.web.controller.AuthController;
import ru.dmitriy.userservice.web.dto.auth.JwtRequest;
import ru.dmitriy.userservice.web.dto.auth.JwtResponse;
import ru.dmitriy.userservice.web.dto.user.UserRegisterDto;

@Validated
@RestController
@RequestMapping("/api/v1/auth")
public class AuthControllerImpl implements AuthController {

    private final AuthService authService;
    private final UserService userService;
    private final MapperRegistry mapperRegistry;

    public AuthControllerImpl(AuthService authService, UserService userService, MapperRegistry mapperRegistry) {
        this.authService = authService;
        this.userService = userService;
        this.mapperRegistry = mapperRegistry;
    }

    @Override
    @PostMapping("/login")
    public JwtResponse login(@Valid @RequestBody JwtRequest jwtRequest) throws UserNotFoundException {
        return authService.login(jwtRequest);
    }

    @Override
    @PostMapping("/register")
    public UserRegisterDto register(@Valid @RequestBody UserRegisterDto userDto) {
        Mappable<User, UserRegisterDto> mapper = mapperRegistry.get("userRegisterDtoMapper");
        User user = mapper.toEntity(userDto);
        Long id = userService.save(user);
        user.setId(id);
        return mapper.toDto(user);
    }

    @Override
    @PostMapping("/refresh")
    public JwtResponse refresh(@NotBlank @RequestBody String refreshToken) throws UserNotFoundException {
        return authService.refresh(refreshToken);
    }
}
