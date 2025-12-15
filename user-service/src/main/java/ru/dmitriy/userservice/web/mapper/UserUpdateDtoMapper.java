package ru.dmitriy.userservice.web.mapper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.dmitriy.commondomain.domain.user.User;
import ru.dmitriy.commondomain.util.Mappable;
import ru.dmitriy.userservice.web.dto.user.UserUpdateDto;

@Service
public class UserUpdateDtoMapper implements Mappable<User, UserUpdateDto> {

    @Autowired
    private UserProfileDtoMapper userProfileDtoMapper;

    @Override
    public User toEntity(UserUpdateDto dto) {
        if (dto == null) {
            return null;
        }
        User user = new User();
        user.setUsername(dto.email());
        if (dto.profile() != null) {
            var userProfile = userProfileDtoMapper.toEntity(dto.profile());
            userProfile.setUser(user);
            user.setProfile(userProfile);
        }
        return user;
    }

    @Override
    public UserUpdateDto toDto(User entity) {
        if (entity == null) {
            return null;
        }
        var userProfileDto = userProfileDtoMapper.toDto(entity.getProfile());
        return new UserUpdateDto(
                entity.getUsername(),
                userProfileDto
        );
    }
}