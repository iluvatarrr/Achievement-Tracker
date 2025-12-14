package ru.dmitriy.groupservice.service;

import ru.dmitriy.commondomain.domain.exception.GroupNotFoundException;
import ru.dmitriy.commondomain.domain.exception.UserNotFoundException;
import ru.dmitriy.commondomain.domain.group.Group;
import ru.dmitriy.commondomain.domain.group.GroupMember;
import ru.dmitriy.commondomain.domain.group.GroupRole;
import javax.naming.ServiceUnavailableException;
import java.util.List;

public interface GroupService {
    List<Group> findAllPublicGroupOrMemberGroup(Long id) throws UserNotFoundException, ServiceUnavailableException;
    Long create(Long userId, Group group) throws UserNotFoundException, ServiceUnavailableException;
    Group getById(Long id) throws GroupNotFoundException;
    void deleteGroup(Long groupId);
    void addMember(Long groupId, Long userId) throws UserNotFoundException, GroupNotFoundException, ServiceUnavailableException;
    void deleteMember(Long groupId, Long userId) throws UserNotFoundException, GroupNotFoundException, ServiceUnavailableException;
    GroupMember setRoleToMember(Long groupId, Long userId, GroupRole groupRole) throws UserNotFoundException, GroupNotFoundException, ServiceUnavailableException;
}
