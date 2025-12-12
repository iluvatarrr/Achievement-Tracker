package ru.dmitriy.userservice.service;

import ru.dmitriy.commondomain.domain.user.User;
import ru.dmitriy.commondomain.domain.exception.UserNotFoundException;

import java.util.List;

public interface UserService extends CRUDService<Long, User> {
    User updateWithoutProfile(Long id, User user) throws UserNotFoundException;
}
