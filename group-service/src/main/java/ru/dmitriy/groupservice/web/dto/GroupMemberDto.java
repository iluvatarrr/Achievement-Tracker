package ru.dmitriy.groupservice.web.dto;

import ru.dmitriy.commondomain.domain.group.GroupRole;

public record GroupMemberDto(
        Long id,
        UserInfo user,
        GroupRole role
) {}