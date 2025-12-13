package ru.dmitriy.userservice.web.controller;

import ru.dmitriy.commondomain.domain.exception.ResourseNotFoundException;
import ru.dmitriy.userservice.web.dto.user.UserDto;
import ru.dmitriy.userservice.web.dto.user.UserUpdateDto;

public interface UserController {
    UserDto getById(Long id) throws ResourseNotFoundException;
    UserDto update(Long id, UserUpdateDto dto) throws ResourseNotFoundException;
    void delete(Long id);
}
