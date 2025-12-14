package ru.dmitriy.notificationservice.web.mapper;

import org.springframework.stereotype.Service;
import ru.dmitriy.commondomain.domain.notification.GroupInvocation;
import ru.dmitriy.commondomain.util.Mappable;
import ru.dmitriy.notificationservice.web.dto.CreateGroupInvocationDto;

@Service
public class CreateGroupInvocationDtoMapper implements Mappable<GroupInvocation, CreateGroupInvocationDto> {

    @Override
    public GroupInvocation toEntity(CreateGroupInvocationDto groupInvocationDto) {
        if (groupInvocationDto == null) {
            return null;
        }
        GroupInvocation groupInvocation = new GroupInvocation();
        groupInvocation.setUsername(groupInvocationDto.username());
        groupInvocation.setExpiresAt(groupInvocationDto.expiresAt());
        return groupInvocation;
    }

    @Override
    public CreateGroupInvocationDto toDto(GroupInvocation groupInvocation) {
        if (groupInvocation == null) {
            return null;
        }
        return new CreateGroupInvocationDto(
                groupInvocation.getUsername(),
                groupInvocation.getExpiresAt()
        );
    }
}
