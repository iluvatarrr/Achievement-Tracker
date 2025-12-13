package ru.dmitriy.userservice.web.dto.user;

import ru.dmitriy.commondomain.domain.user.Role;
import ru.dmitriy.commondomain.domain.user.UserStatus;
import java.time.LocalDateTime;
import java.util.Set;

public record UserDto(
    String email,
    String password,
    LocalDateTime createdAt,
    Set<Role> roles,
    UserProfileDto profile,
    UserStatus userStatus
) {
}
