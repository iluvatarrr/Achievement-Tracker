package ru.dmitriy.groupservice.web.dto;

public record CreateGroupDto(
        Long id,
        String title,
        String description,
        String organisation,
        Boolean isPublic,
        UserInfo createdBy
) {}