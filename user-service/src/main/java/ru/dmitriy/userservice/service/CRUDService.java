package ru.dmitriy.userservice.service;

import ru.dmitriy.commondomain.domain.exception.UserNotFoundException;

import java.util.List;

public interface CRUDService<ID, ENTITY> {
    List<ENTITY> findAll();

    ENTITY getById(ID id) throws UserNotFoundException;

    Long save(ENTITY entity);

    ENTITY update(ID id, ENTITY entity) throws UserNotFoundException;

    void delete(ID id);
}
