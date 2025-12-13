package ru.dmitriy.userservice.web.dto.user;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import org.hibernate.validator.constraints.Length;

public record UserRegisterDto(
        @Email(message = "Email must contain @")
        @Length(min = 6)
        @NotBlank(message = "Email can`t be Blank")
        String email,
        @Length(min = 6)
        @NotBlank(message = "Password can`t be Blank")
        String password
) {
}
