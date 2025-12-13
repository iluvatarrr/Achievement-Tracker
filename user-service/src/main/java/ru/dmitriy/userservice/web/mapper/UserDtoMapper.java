package ru.dmitriy.userservice.web.mapper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.dmitriy.commondomain.domain.user.User;
import ru.dmitriy.commondomain.util.Mappable;
import ru.dmitriy.userservice.web.dto.user.UserDto;

@Service
public class UserDtoMapper implements Mappable<User, UserDto> {

    @Autowired
    private UserProfileDtoMapper userProfileDtoMapper;

    @Override
    public User toEntity(UserDto dto) {
        if (dto == null) {
            return null;
        }
        User user = new User();
        user.setUsername(dto.email());
        user.setPassword(dto.password());
        user.setCreatedAt(dto.createdAt());
        user.setRoles(dto.roles());
        user.setUserStatus(dto.userStatus());
        if (dto.profile() != null) {
            var userProfile = userProfileDtoMapper.toEntity(dto.profile());
            userProfile.setUser(user);
            user.setProfile(userProfile);
        }
        return user;
    }

    @Override
    public UserDto toDto(User entity) {
        if (entity == null) {
            return null;
        }
        var userProfileDto = userProfileDtoMapper.toDto(entity.getProfile());
        return new UserDto(
                entity.getUsername(),
                entity.getPassword(),
                entity.getCreatedAt(),
                entity.getRoles(),
                userProfileDto,
                entity.getUserStatus()
        );
    }
}