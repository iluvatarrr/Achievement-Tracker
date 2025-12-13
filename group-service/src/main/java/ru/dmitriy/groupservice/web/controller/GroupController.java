package ru.dmitriy.groupservice.web.controller;

import org.springframework.web.bind.annotation.PathVariable;
import ru.dmitriy.commondomain.domain.exception.GroupNotFoundException;
import ru.dmitriy.commondomain.domain.exception.UserNotFoundException;
import ru.dmitriy.commondomain.domain.group.GroupRole;
import ru.dmitriy.groupservice.web.dto.CreateGroupDto;
import ru.dmitriy.groupservice.web.dto.GroupDto;
import ru.dmitriy.groupservice.web.dto.GroupMemberDto;
import javax.naming.ServiceUnavailableException;
import java.util.List;

public interface GroupController {
    Long create(CreateGroupDto createGroupDto);
    List<GroupDto> findAllPublicGroupOrMemberGroup(Long userId) throws UserNotFoundException, ServiceUnavailableException;
    GroupMemberDto setRoleToMember(Long groupId, Long userId, GroupRole groupRole) throws UserNotFoundException, GroupNotFoundException, ServiceUnavailableException;
//    GroupGoalDto createGroupGoal(Long idGroup, Long userId, GroupGoalDto groupGoalDto) throws UserNotFoundException, GroupNotFoundException;
    void deleteMember(Long groupId, Long userId) throws UserNotFoundException, GroupNotFoundException, ServiceUnavailableException;
    void deleteGroup(Long groupId);
    GroupDto getById(@PathVariable Long groupId) throws GroupNotFoundException;
}
