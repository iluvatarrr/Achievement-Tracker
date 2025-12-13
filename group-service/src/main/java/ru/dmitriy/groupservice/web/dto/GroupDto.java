package ru.dmitriy.groupservice.web.dto;

import ru.dmitriy.commondomain.domain.group.GroupStatus;

import java.time.LocalDateTime;
import java.util.Set;

public record GroupDto(
        Long id,
        String title,
        String description,
        String organisation,
        Boolean isPublic,
        GroupStatus groupStatus,
        UserInfo createdBy,
        LocalDateTime createdAt,
        Set<GroupMemberDto> members
) {}