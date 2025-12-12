package ru.dmitriy.goalservice.service;

import ru.dmitriy.commondomain.domain.exception.ResourseNotFoundException;

import javax.naming.ServiceUnavailableException;
import java.util.List;

public interface CRUDService<ID, ENTITY> {
    List<ENTITY> findAll();

    ENTITY getById(ID id) throws ResourseNotFoundException;

    Long save(ENTITY entity, ID id) throws ResourseNotFoundException, ServiceUnavailableException;

    ENTITY update(ID id, ENTITY entity) throws ResourseNotFoundException;

    void delete(ID id);
}
