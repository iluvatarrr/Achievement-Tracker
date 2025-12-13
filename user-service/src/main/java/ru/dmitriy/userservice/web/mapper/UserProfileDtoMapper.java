package ru.dmitriy.userservice.web.mapper;

import org.springframework.stereotype.Service;
import ru.dmitriy.commondomain.domain.user.UserProfile;
import ru.dmitriy.commondomain.util.Mappable;
import ru.dmitriy.userservice.web.dto.user.UserProfileDto;

@Service
public class UserProfileDtoMapper implements Mappable<UserProfile, UserProfileDto> {

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