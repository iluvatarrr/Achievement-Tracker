package ru.dmitriy.userservice.web.mapper.impl;

import org.springframework.stereotype.Service;
import ru.dmitriy.commondomain.domain.user.UserProfile;
import ru.dmitriy.userservice.web.dto.UserProfileDto;
import ru.dmitriy.userservice.web.mapper.Mappable;

@Service
public class UserProfileMapper implements Mappable<UserProfile, UserProfileDto> {

    @Override
    public UserProfile toEntity(UserProfileDto dto) {
        if (dto == null) {
            return null;
        }
        UserProfile profile = new UserProfile();
        profile.setFirstName(dto.firstName());
        profile.setLastName(dto.lastName());
        return profile;
    }

    @Override
    public UserProfileDto toDto(UserProfile entity) {
        if (entity == null) {
            return null;
        }
        return new UserProfileDto(
                entity.getFirstName(),
                entity.getLastName()
        );
    }
}