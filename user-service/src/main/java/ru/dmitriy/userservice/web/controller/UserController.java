package ru.dmitriy.userservice.web.controller;

import ru.dmitriy.commondomain.domain.exception.ResourseNotFoundException;
import ru.dmitriy.userservice.web.dto.user.UserDto;

public interface UserController {
    UserDto getById(Long id) throws ResourseNotFoundException;
    Long create(UserDto dto);
    UserDto update(Long id, UserDto dto) throws ResourseNotFoundException;
    void delete(Long id);
}
