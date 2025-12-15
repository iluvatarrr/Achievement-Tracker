package ru.dmitriy.userservice.web.dto.user;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.hibernate.validator.constraints.Length;

public record UserUpdateDto(
        @Email(message = "Email must contain @")
        @Length(min = 6)
        @NotBlank(message = "Email can`t be Blank")
        String email,
        @NotNull(message = "Roles cannot be null")
        UserProfileDto profile
) {}
