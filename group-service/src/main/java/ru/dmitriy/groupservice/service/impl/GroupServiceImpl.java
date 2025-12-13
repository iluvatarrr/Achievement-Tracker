package ru.dmitriy.groupservice.service.impl;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.dmitriy.commondomain.domain.exception.GroupNotFoundException;
import ru.dmitriy.commondomain.domain.exception.UserNotFoundException;
import ru.dmitriy.commondomain.domain.group.Group;
import ru.dmitriy.commondomain.domain.group.GroupMember;
import ru.dmitriy.commondomain.domain.group.GroupRole;
import ru.dmitriy.commondomain.domain.group.GroupStatus;
import ru.dmitriy.commondomain.listener.UserValidationResponseEventListener;
import ru.dmitriy.groupservice.repository.GroupRepository;
import ru.dmitriy.groupservice.service.GroupService;
import javax.naming.ServiceUnavailableException;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class GroupServiceImpl implements GroupService {

    private final GroupRepository groupRepository;
    private final UserValidationResponseEventListener userValidationResponseEventListener;

    public GroupServiceImpl(GroupRepository groupRepository, UserValidationResponseEventListener userValidationResponseEventListener) {
        this.groupRepository = groupRepository;
        this.userValidationResponseEventListener = userValidationResponseEventListener;
    }

    @Override
    @Transactional(readOnly = true)
    public Group getById(Long id) throws GroupNotFoundException {
        return groupRepository.findById(id).orElseThrow(() ->
                new GroupNotFoundException("Группа не найдена по id: " + id));
    }

    @Override
    @Transactional(readOnly = true)
    public List<Group> findAllPublicGroupOrMemberGroup(Long id) throws UserNotFoundException, ServiceUnavailableException {
        userValidationResponseEventListener.checkUserId(id);
        return groupRepository.findAllPublicGroupOrMember(id);
    }

    @Override
    @Transactional
    public Long create(Group group) {
        group.setGroupStatus(GroupStatus.ACTIVE);
        group.setCreatedAt(LocalDateTime.now());
//        group.setCreatedBy(new User()); // при внеднерии auth заменить на контекстное значение
        return groupRepository.save(group).getId();
    }

    @Override
    @Transactional
    public void deleteGroup(Long groupId) {
        groupRepository.deleteById(groupId);
    }

    @Override
    @Transactional
    public void deleteMember(Long groupId, Long userId) throws UserNotFoundException, GroupNotFoundException, ServiceUnavailableException {
        userValidationResponseEventListener.checkUserId(userId);
        var group = getById(groupId);
        var groupMember = findUserInGroupByUserId(userId, group);
        group.getMembers().remove(groupMember);
    }

    @Override
    @Transactional
    public GroupMember setRoleToMember(Long groupId, Long userId, GroupRole groupRole) throws UserNotFoundException, GroupNotFoundException, ServiceUnavailableException {
        userValidationResponseEventListener.checkUserId(userId);
        var group = getById(groupId);
        var groupMember = findUserInGroupByUserId(userId, group);
        groupMember.setGroupRole(groupRole);
        return groupMember;
    }

    private GroupMember findUserInGroupByUserId(Long userId, Group group) throws UserNotFoundException {
        return group.getMembers()
                .stream()
                .filter(m -> m.getUser().getId().equals(userId))
                .findFirst()
                .orElseThrow(() -> new UserNotFoundException("Член группы по id пользователя не найден, id:" + userId));
    }
}
