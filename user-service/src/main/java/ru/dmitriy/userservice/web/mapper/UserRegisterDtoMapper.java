package ru.dmitriy.userservice.web.mapper;

import org.springframework.stereotype.Service;
import ru.dmitriy.commondomain.domain.user.User;
import ru.dmitriy.commondomain.util.Mappable;
import ru.dmitriy.userservice.web.dto.user.UserRegisterDto;

@Service
public class UserRegisterDtoMapper implements Mappable<User, UserRegisterDto> {
    @Override
    public User toEntity(UserRegisterDto dto) {
        if (dto == null) {
            return null;
        }
        User user = new User();
        user.setUsername(dto.email());
        user.setPassword(dto.password());
        return user;
    }

    @Override
    public UserRegisterDto toDto(User entity) {
        if (entity == null) {
            return null;
        }
        return new UserRegisterDto(
                entity.getUsername(),
                entity.getPassword()
        );
    }
}
