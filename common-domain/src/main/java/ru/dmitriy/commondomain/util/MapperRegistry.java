package ru.dmitriy.commondomain.util;

import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class MapperRegistry {

    private final Map<String, Mappable<?, ?>> mappers;

    public MapperRegistry(Map<String, Mappable<?, ?>> mappers) {
        this.mappers = mappers;
    }

    public <Entity, Dto> Mappable<Entity, Dto> get(String name) {
        return (Mappable<Entity, Dto>) mappers.get(name);
    }
}