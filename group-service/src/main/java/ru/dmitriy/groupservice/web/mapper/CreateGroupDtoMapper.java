package ru.dmitriy.groupservice.web.mapper;

import org.springframework.stereotype.Service;
import ru.dmitriy.commondomain.domain.group.Group;
import ru.dmitriy.commondomain.util.Mappable;
import ru.dmitriy.groupservice.web.dto.CreateGroupDto;
import ru.dmitriy.groupservice.web.dto.UserInfo;

@Service
public class CreateGroupDtoMapper implements Mappable<Group, CreateGroupDto> {
    @Override
    public Group toEntity(CreateGroupDto createGroupDto) {
        if (createGroupDto == null) {
            return null;
        }

        Group group = new Group();
        group.setTitle(createGroupDto.title());
        group.setDescription(createGroupDto.description());
        group.setOrganisation(createGroupDto.organisation());
        group.setPublic(createGroupDto.isPublic());
        return group;
    }

    @Override
    public CreateGroupDto toDto(Group group) {
        if (group == null) {
            return null;
        }
        return new CreateGroupDto(
                group.getTitle(),
                group.getDescription(),
                group.getOrganisation(),
                group.getPublic()
        );
    }
}
