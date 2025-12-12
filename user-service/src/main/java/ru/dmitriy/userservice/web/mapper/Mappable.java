package ru.dmitriy.userservice.web.mapper;

import java.util.List;
import java.util.stream.Collectors;


public interface Mappable<Entity, Dto> {
    Entity toEntity(Dto dto);

    Dto toDto(Entity entity);

    default List<Dto> toDto(List<Entity> entities) {
        return entities.stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    default List<Entity> toEntity(List<Dto> dtos) {
        return dtos.stream()
                .map(this::toEntity)
                .collect(Collectors.toList());
    }
}
