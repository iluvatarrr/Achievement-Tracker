package ru.dmitriy.groupservice.web.controller.impl;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.dmitriy.commondomain.domain.exception.GroupNotFoundException;
import ru.dmitriy.commondomain.domain.exception.UserNotFoundException;
import ru.dmitriy.commondomain.domain.group.Group;
import ru.dmitriy.commondomain.domain.group.GroupMember;
import ru.dmitriy.commondomain.domain.group.GroupRole;
import ru.dmitriy.commondomain.util.Mappable;
import ru.dmitriy.commondomain.util.MapperRegistry;
import ru.dmitriy.groupservice.service.GroupService;
import ru.dmitriy.groupservice.web.controller.GroupController;
import ru.dmitriy.groupservice.web.dto.CreateGroupDto;
import ru.dmitriy.groupservice.web.dto.GroupDto;
import ru.dmitriy.groupservice.web.dto.GroupMemberDto;
import javax.naming.ServiceUnavailableException;
import java.util.List;

@Validated
@RestController
@RequestMapping("/api/v1/group")
public class GroupControllerImpl implements GroupController {

    private final GroupService groupService;
    private final MapperRegistry mapperRegistry;

    public GroupControllerImpl(GroupService groupService, MapperRegistry mapperRegistry) {
        this.groupService = groupService;
        this.mapperRegistry = mapperRegistry;
    }

    @Override
    @GetMapping("/{groupId}")
    public GroupDto getById(@Min(1) @PathVariable Long groupId) throws GroupNotFoundException {
        Mappable<Group, GroupDto> mapper = mapperRegistry.get("groupDtoMapper");
        return mapper.toDto(groupService.getById(groupId));
    }

    @Override
    @PostMapping
    public Long create(@Valid @RequestBody CreateGroupDto createGroupDto) {
        Mappable<Group, CreateGroupDto> mapper = mapperRegistry.get("createGroupDtoMapper");
        var group = mapper.toEntity(createGroupDto);
        return groupService.create(group);
    }

    @Override
    @GetMapping("/user-id/{userId}")
    public List<GroupDto> findAllPublicGroupOrMemberGroup(@Min(1) @PathVariable Long userId) throws UserNotFoundException, ServiceUnavailableException {
        Mappable<Group, GroupDto> mapper = mapperRegistry.get("groupDtoMapper");
        return mapper.toDto(groupService.findAllPublicGroupOrMemberGroup(userId));
    }

    @Override
    @PatchMapping("/{groupId}/user-id/{userId}")
    public GroupMemberDto setRoleToMember(@Min(1) @PathVariable Long groupId, @Min(1) @PathVariable Long userId, @NotNull @RequestParam GroupRole groupRole) throws UserNotFoundException, GroupNotFoundException, ServiceUnavailableException {
        Mappable<GroupMember, GroupMemberDto> mapper = mapperRegistry.get("groupMemberDtoMapper");
        return mapper.toDto(groupService.setRoleToMember(groupId, userId, groupRole));
    }

//
//    @Override
//    public GroupGoalDto createGroupGoal(Long idGroup, Long userId, GroupGoalDto groupGoalDto) throws UserNotFoundException, GroupNotFoundException {
//        return null;
//    }

    @Override
    @DeleteMapping("/{groupId}/user-id/{userId}")
    public void deleteMember(@Min(1) @PathVariable Long groupId,@Min(1) @PathVariable Long userId) throws UserNotFoundException, GroupNotFoundException, ServiceUnavailableException {
        groupService.deleteMember(groupId, userId);
    }

    @Override
    @DeleteMapping("/{groupId}")
    public void deleteGroup(@Min(1) @PathVariable Long groupId) {
        groupService.deleteGroup(groupId);
    }
}
