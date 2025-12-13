package ru.dmitriy.userservice.web.controller.impl;

import org.springframework.web.bind.annotation.*;
import ru.dmitriy.commondomain.domain.exception.UserNotFoundException;
import ru.dmitriy.userservice.service.UserService;
import ru.dmitriy.userservice.web.controller.UserController;
import ru.dmitriy.userservice.web.dto.UserDto;
import ru.dmitriy.userservice.web.mapper.UserMapper;

@RestController
@RequestMapping("/api/v1/user")
public class UserControllerImpl implements UserController {
    private final UserService userService;
    private final UserMapper mapper;

    public UserControllerImpl(UserService userService, UserMapper mapper) {
        this.userService = userService;
        this.mapper = mapper;
    }

    @GetMapping("/{id}")
    public UserDto getById(@PathVariable Long id) throws UserNotFoundException {
        var a = userService.getById(id);
        return mapper.toDto(a);
    }

    @PostMapping
    public Long create(@RequestBody UserDto user) {
        var userEntity = mapper.toEntity(user);
        return userService.save(userEntity);
    }

    @PatchMapping("/{id}")
    public UserDto update(@PathVariable Long id, @RequestBody UserDto user) throws UserNotFoundException {
        var userEntity = mapper.toEntity(user);
        return mapper.toDto(userService.update(id, userEntity));
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        userService.delete(id);
    }
}
