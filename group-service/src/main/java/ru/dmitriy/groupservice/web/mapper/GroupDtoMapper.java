package ru.dmitriy.groupservice.web.mapper;

import org.springframework.stereotype.Service;
import ru.dmitriy.commondomain.domain.group.Group;
import ru.dmitriy.commondomain.domain.group.GroupMember;
import ru.dmitriy.commondomain.domain.user.User;
import ru.dmitriy.commondomain.util.Mappable;
import ru.dmitriy.groupservice.web.dto.GroupDto;
import ru.dmitriy.groupservice.web.dto.GroupMemberDto;
import ru.dmitriy.groupservice.web.dto.UserInfo;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class GroupDtoMapper implements Mappable<Group, GroupDto> {
    @Override
    public Group toEntity(GroupDto groupDto) {
        if (groupDto == null) {
            return null;
        }

        Group group = new Group();
        group.setId(groupDto.id());
        group.setTitle(groupDto.title());
        group.setDescription(groupDto.description());
        group.setOrganisation(groupDto.organisation());
        group.setPublic(groupDto.isPublic());
        group.setGroupStatus(groupDto.groupStatus());
        group.setCreatedAt(groupDto.createdAt());
        return group;
    }

    @Override
    public GroupDto toDto(Group group) {
        if (group == null) {
            return null;
        }

        UserInfo createdByInfo = mapToUserInfo(group.getCreatedBy());
        Set<GroupMemberDto> memberDtos = mapMembersToDto(group.getMembers());

        return new GroupDto(
                group.getId(),
                group.getTitle(),
                group.getDescription(),
                group.getOrganisation(),
                group.getPublic(),
                group.getGroupStatus(),
                createdByInfo,
                group.getCreatedAt(),
                memberDtos
        );
    }
    private UserInfo mapToUserInfo(User user) {
        if (user == null) {
            return null;
        }

        return new UserInfo(
                user.getId(),
                user.getEmail()
        );
    }

    private Set<GroupMemberDto> mapMembersToDto(Set<GroupMember> members) {
        if (members == null) {
            return Set.of();
        }

        return members.stream()
                .map(this::mapToGroupMemberDto)
                .collect(Collectors.toSet());
    }

    private GroupMemberDto mapToGroupMemberDto(GroupMember member) {
        if (member == null) {
            return null;
        }

        return new GroupMemberDto(
                member.getId(),
                mapToUserInfo(member.getUser()),
                member.getGroupRole()
        );
    }
}