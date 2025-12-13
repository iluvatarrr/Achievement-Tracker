package ru.dmitriy.userservice.web.mapper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.dmitriy.commondomain.domain.user.User;
import ru.dmitriy.commondomain.util.Mappable;
import ru.dmitriy.userservice.web.dto.UserDto;

@Service
public class UserMapper implements Mappable<User, UserDto> {

    @Autowired
    private UserProfileMapper userProfileMapper;

    @Override
    public User toEntity(UserDto dto) {
        if (dto == null) {
            return null;
        }
        User user = new User();
        user.setEmail(dto.email());
        user.setPassword(dto.password());
        user.setCreatedAt(dto.createdAt());
        user.setRoles(dto.roles());
        user.setUserStatus(dto.userStatus());
        if (dto.profile() != null) {
            var userProfile = userProfileMapper.toEntity(dto.profile());
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
        var userProfileDto = userProfileMapper.toDto(entity.getProfile());
        return new UserDto(
                entity.getEmail(),
                entity.getPassword(),
                entity.getCreatedAt(),
                entity.getRoles(),
                userProfileDto,
                entity.getUserStatus()
        );
    }
}