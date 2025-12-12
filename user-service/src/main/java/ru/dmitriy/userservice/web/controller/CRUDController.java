package ru.dmitriy.userservice.web.controller;

import ru.dmitriy.commondomain.domain.exception.ResourseNotFoundException;

public interface CRUDController<ID, DTO> {
    DTO getById(ID id) throws ResourseNotFoundException;
    ID create(DTO dto);
    DTO update(ID id, DTO dto) throws ResourseNotFoundException;
    void delete(ID id);
}
