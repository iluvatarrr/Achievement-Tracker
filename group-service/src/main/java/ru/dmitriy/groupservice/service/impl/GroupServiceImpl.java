package ru.dmitriy.groupservice.service.impl;

import jakarta.persistence.EntityManager;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.dmitriy.commondomain.domain.exception.GroupNotFoundException;
import ru.dmitriy.commondomain.domain.exception.UserNotFoundException;
import ru.dmitriy.commondomain.domain.group.Group;
import ru.dmitriy.commondomain.domain.group.GroupMember;
import ru.dmitriy.commondomain.domain.group.GroupRole;
import ru.dmitriy.commondomain.domain.group.GroupStatus;
import ru.dmitriy.commondomain.domain.user.User;
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
    private final EntityManager entityManager;

    public GroupServiceImpl(GroupRepository groupRepository, UserValidationResponseEventListener userValidationResponseEventListener, EntityManager entityManager) {
        this.groupRepository = groupRepository;
        this.userValidationResponseEventListener = userValidationResponseEventListener;
        this.entityManager = entityManager;
    }

    @Override
    @Transactional(readOnly = true)
    public Group getById(Long id) throws GroupNotFoundException {
        return groupRepository.findById(id).orElseThrow(() ->
                new GroupNotFoundException("Группа не найдена по id: " + id));
    }

    @Override
    @Transactional(readOnly = true)
    public List<Group> findAllPublicGroupOrMemberGroup(Long userId) throws UserNotFoundException, ServiceUnavailableException {
        userValidationResponseEventListener.checkUserId(userId);
        return groupRepository.findAllPublicGroupOrMember(userId);
    }

    @Override
    @Transactional
    public Long create(Long userId, Group group) throws UserNotFoundException, ServiceUnavailableException {
        userValidationResponseEventListener.checkUserId(userId);
        User userProxy = entityManager.getReference(User.class, userId);
        group.setGroupStatus(GroupStatus.ACTIVE);
        group.setCreatedAt(LocalDateTime.now());
        group.setCreatedBy(userProxy);
        group.addMember(userProxy, GroupRole.OWNER);
        return groupRepository.save(group).getId();
    }

    @Override
    @Transactional
    public void deleteGroup(Long groupId) {
        groupRepository.deleteById(groupId);
    }

    @Override
    @Transactional
    public void addMember(Long groupId, Long userId) throws UserNotFoundException, GroupNotFoundException, ServiceUnavailableException {
        userValidationResponseEventListener.checkUserId(userId);
        User userProxy = entityManager.getReference(User.class, userId);
        var group = getById(groupId);
        group.addMember(userProxy, GroupRole.MEMBER);
    }

    @Override
    @Transactional
    public void deleteMember(Long groupId, Long userId) throws UserNotFoundException, GroupNotFoundException, ServiceUnavailableException {
        userValidationResponseEventListener.checkUserId(userId);
        var group = getById(groupId);
        var groupMember = findUserInGroupByUserId(userId, group);
        if (!groupMember.getGroupRole().equals(GroupRole.OWNER)) {
            group.getMembers().remove(groupMember);
        }
    }

    @Override
    @Transactional
    public GroupMember setRoleToMember(Long groupId, Long userId, GroupRole groupRole) throws UserNotFoundException, GroupNotFoundException, ServiceUnavailableException {
        userValidationResponseEventListener.checkUserId(userId);
        var group = getById(groupId);
        var groupMember = findUserInGroupByUserId(userId, group);
        if (!groupMember.getGroupRole().equals(GroupRole.OWNER)) {
            groupMember.setGroupRole(groupRole);
        }
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
