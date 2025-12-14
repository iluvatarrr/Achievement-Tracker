package ru.dmitriy.groupservice.web.controller;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import ru.dmitriy.commondomain.domain.exception.GroupNotFoundException;
import ru.dmitriy.commondomain.domain.exception.UserNotFoundException;
import ru.dmitriy.commondomain.domain.group.GroupRole;
import ru.dmitriy.groupservice.web.dto.CreateGroupDto;
import ru.dmitriy.groupservice.web.dto.GroupDto;
import ru.dmitriy.groupservice.web.dto.GroupMemberDto;
import javax.naming.ServiceUnavailableException;
import java.util.List;

public interface GroupController {
    Long create(@Min(1) Long userId, @Valid CreateGroupDto createGroupDto) throws UserNotFoundException, ServiceUnavailableException;
    List<GroupDto> findAllPublicGroupOrMemberGroup(@Min(1) Long userId) throws UserNotFoundException, ServiceUnavailableException;
    GroupMemberDto setRoleToMember(@Min(1) Long groupId,@Min(1) Long userId, @NotNull GroupRole groupRole) throws UserNotFoundException, GroupNotFoundException, ServiceUnavailableException;
//    GroupGoalDto createGroupGoal(Long idGroup, Long userId, GroupGoalDto groupGoalDto) throws UserNotFoundException, GroupNotFoundException;
    void deleteMember(@Min(1) Long groupId, @Min(1) Long userId) throws UserNotFoundException, GroupNotFoundException, ServiceUnavailableException;
    void deleteGroup(@Min(1) Long groupId);
    GroupDto getById(@Min(1) Long groupId) throws GroupNotFoundException;
}
