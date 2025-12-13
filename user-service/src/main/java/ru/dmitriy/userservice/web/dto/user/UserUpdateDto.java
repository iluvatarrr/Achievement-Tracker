package ru.dmitriy.userservice.web.dto.user;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import org.hibernate.validator.constraints.Length;
import ru.dmitriy.commondomain.domain.user.Role;
import ru.dmitriy.commondomain.domain.user.UserStatus;
import java.util.Set;

public record UserUpdateDto(
        @Email(message = "Email must contain @")
        @Length(min = 6)
        @NotBlank(message = "Email can`t be Blank")
        String email,
        @NotNull(message = "Roles cannot be null")
        @Size(min = 1, message = "At least one role is required")
        Set<Role> roles,
        UserProfileDto profile,
        @NotNull(message = "userStatus can't be null")
        UserStatus userStatus
) {}
