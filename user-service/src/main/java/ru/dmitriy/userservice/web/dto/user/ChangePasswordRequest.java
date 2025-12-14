package ru.dmitriy.userservice.web.dto.user;

import jakarta.validation.constraints.NotBlank;
import org.hibernate.validator.constraints.Length;

public record ChangePasswordRequest(
        @NotBlank(message = "currentPassword can't be blank")
        @Length(min = 6)
        String currentPassword,
        @NotBlank(message = "newPassword can't be blank")
        @Length(min = 6)
        String newPassword
) {
}