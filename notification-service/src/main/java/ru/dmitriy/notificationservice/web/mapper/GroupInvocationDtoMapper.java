package ru.dmitriy.notificationservice.web.mapper;

import org.springframework.stereotype.Service;
import ru.dmitriy.commondomain.domain.notification.GroupInvocation;
import ru.dmitriy.commondomain.util.Mappable;
import ru.dmitriy.notificationservice.web.dto.GroupInvocationDto;

@Service
public class GroupInvocationDtoMapper implements Mappable<GroupInvocation, GroupInvocationDto> {

    @Override
    public GroupInvocation toEntity(GroupInvocationDto dto) {
        if (dto == null) {
            return null;
        }
        GroupInvocation invocation = new GroupInvocation();
        invocation.setUsername(dto.username());
        invocation.setInvitedByName(dto.invitedByName());
        invocation.setStatus(dto.status());
        invocation.setCreatedAt(dto.createdAt());
        invocation.setExpiresAt(dto.expiresAt());
        return invocation;
    }

    @Override
    public GroupInvocationDto toDto(GroupInvocation invocation) {
        if (invocation == null) {
            return null;
        }

        return new GroupInvocationDto(
                invocation.getId(),
                invocation.getGroup().getTitle(),
                invocation.getUsername(),
                invocation.getInvitedByName(),
                invocation.getStatus(),
                invocation.getCreatedAt(),
                invocation.getExpiresAt()
        );
    }
}