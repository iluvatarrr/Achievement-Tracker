package ru.dmitriy.userservice.web.controller;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import org.springframework.http.ResponseEntity;
import ru.dmitriy.commondomain.domain.exception.ResourseNotFoundException;
import ru.dmitriy.commondomain.domain.exception.UserNotFoundException;
import ru.dmitriy.userservice.web.dto.user.ChangePasswordRequest;
import ru.dmitriy.userservice.web.dto.user.UserDto;
import ru.dmitriy.userservice.web.dto.user.UserUpdateDto;

public interface UserController {
    UserDto getById(@Min(1) Long id) throws ResourseNotFoundException;
    UserDto update(@Min(1) Long id, @Valid UserUpdateDto dto) throws ResourseNotFoundException;
    void delete(@Min(1) Long id);
    ResponseEntity<?> changePassword(@Valid ChangePasswordRequest req) throws UserNotFoundException;
}
