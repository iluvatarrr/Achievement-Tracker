package ru.dmitriy.userservice.web.controller.impl;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.dmitriy.commondomain.domain.exception.UserNotFoundException;
import ru.dmitriy.commondomain.domain.user.Role;
import ru.dmitriy.commondomain.domain.user.User;
import ru.dmitriy.commondomain.domain.user.UserStatus;
import ru.dmitriy.commondomain.util.Mappable;
import ru.dmitriy.commondomain.util.MapperRegistry;
import ru.dmitriy.userservice.service.UserService;
import ru.dmitriy.userservice.web.controller.UserController;
import ru.dmitriy.userservice.web.dto.user.ChangePasswordRequest;
import ru.dmitriy.userservice.web.dto.user.UserDto;
import ru.dmitriy.userservice.web.dto.user.UserUpdateDto;

import java.util.List;

@RestController
@Validated
@RequestMapping("/api/v1/user")
public class UserControllerImpl implements UserController {
    private final UserService userService;
    private final MapperRegistry mapperRegistry;

    public UserControllerImpl(UserService userService, MapperRegistry mapperRegistry) {
        this.userService = userService;
        this.mapperRegistry = mapperRegistry;
    }

    @Override
    @GetMapping("/{id}")
    @PreAuthorize("@customSecurityExpression.canAccessUser(#id)")
    public UserDto getById(@Min(1) @PathVariable Long id) throws UserNotFoundException {
        Mappable<User, UserDto> mapper = mapperRegistry.get("userDtoMapper");
        var currentUser = userService.getById(id);
        return mapper.toDto(currentUser);
    }

    @GetMapping("/all")
    @PreAuthorize("hasRole('ADMIN')")
    public List<UserDto> getAll() {
        Mappable<User, UserDto> mapper = mapperRegistry.get("userDtoMapper");
        var users = userService.findAll();
        return mapper.toDto(users);
    }

    @PatchMapping("/set-role/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public UserDto addRole(@Min(1) @PathVariable Long id, @NotNull @RequestParam Role role) throws UserNotFoundException {
        Mappable<User, UserDto> mapper = mapperRegistry.get("userDtoMapper");
        var users = userService.addRoleById(id, role);
        return mapper.toDto(users);
    }

    @PatchMapping("/set-user-status/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public UserDto setUserStatus(@Min(1) @PathVariable Long id, @NotNull @RequestParam UserStatus userStatus) throws UserNotFoundException {
        Mappable<User, UserDto> mapper = mapperRegistry.get("userDtoMapper");
        var users = userService.setUserStatus(id, userStatus);
        return mapper.toDto(users);
    }

    @Override
    @PostMapping("/change/password")
    public ResponseEntity<?> changePassword(@Valid @RequestBody ChangePasswordRequest req) throws UserNotFoundException {
        return userService.changePassword(req);
    }

    @Override
    @PatchMapping("/{id}")
    @PreAuthorize("@customSecurityExpression.canAccessUser(#id)")
    public UserDto update(@Min(1) @PathVariable Long id, @Valid @RequestBody UserUpdateDto user) throws UserNotFoundException {
        Mappable<User, UserUpdateDto> userUpdateDtoMapper = mapperRegistry.get("userUpdateDtoMapper");
        var userEntity = userUpdateDtoMapper.toEntity(user);
        Mappable<User, UserDto> userDtoMapper = mapperRegistry.get("userDtoMapper");
        return userDtoMapper.toDto(userService.update(id, userEntity));
    }

    @Override
    @DeleteMapping("/{id}")
    @PreAuthorize("@customSecurityExpression.canAccessUser(#id)")
    public void delete(@Min(1) @PathVariable Long id) {
        userService.delete(id);
    }
}
