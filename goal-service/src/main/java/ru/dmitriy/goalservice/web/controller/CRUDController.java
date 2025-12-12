package ru.dmitriy.goalservice.web.controller;

import ru.dmitriy.commondomain.domain.exception.ResourseNotFoundException;

import javax.naming.ServiceUnavailableException;

public interface CRUDController<ID, DTO> {
    DTO getById(ID id) throws ResourseNotFoundException;
    ID create(DTO dto, ID id) throws ResourseNotFoundException, ServiceUnavailableException;
    DTO update(ID id, DTO dto) throws ResourseNotFoundException;
    void delete(ID id);
}
