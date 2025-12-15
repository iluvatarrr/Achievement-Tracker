package ru.dmitriy.userservice.web.controller;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import ru.dmitriy.commondomain.domain.exception.ResourseNotFoundException;
import ru.dmitriy.commondomain.domain.exception.UserNotFoundException;
import ru.dmitriy.commondomain.domain.user.Role;
import ru.dmitriy.commondomain.domain.user.UserStatus;
import ru.dmitriy.userservice.web.dto.user.ChangePasswordRequest;
import ru.dmitriy.userservice.web.dto.user.UserDto;
import ru.dmitriy.userservice.web.dto.user.UserUpdateDto;

import java.util.List;

public interface UserController {
    List<UserDto> getAll();
    UserDto getById(@Min(1) Long id) throws ResourseNotFoundException;
    UserDto update(@Min(1) Long id, @Valid UserUpdateDto dto) throws ResourseNotFoundException;
    void delete(@Min(1) Long id);
    UserDto addRole(@Min(1) Long id, @NotNull Role role) throws UserNotFoundException;
    UserDto setUserStatus(@Min(1) Long id, @NotNull UserStatus userStatus) throws UserNotFoundException;
    ResponseEntity<?> changePassword(@Valid ChangePasswordRequest req) throws UserNotFoundException;
}
