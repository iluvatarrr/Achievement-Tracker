package ru.dmitriy.notificationservice.service.impl;

import jakarta.persistence.EntityManager;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.dmitriy.commondomain.domain.exception.GroupInvocationNotFoundException;
import ru.dmitriy.commondomain.domain.group.Group;
import ru.dmitriy.commondomain.domain.group.GroupStatus;
import ru.dmitriy.commondomain.domain.notification.GroupInvitationStatus;
import ru.dmitriy.commondomain.domain.notification.GroupInvocation;
import ru.dmitriy.notificationservice.repository.GroupInvocationRepository;
import ru.dmitriy.notificationservice.service.NotificationService;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class NotificationServiceImpl implements NotificationService {

    private final GroupInvocationRepository groupInvocationRepository;
    private final EntityManager entityManager;

    public NotificationServiceImpl(GroupInvocationRepository groupInvocationRepository, EntityManager entityManager) {
        this.groupInvocationRepository = groupInvocationRepository;
        this.entityManager = entityManager;
    }

    @Override
    @Transactional(readOnly = true)
    public GroupInvocation getById(Long id) throws GroupInvocationNotFoundException {
        return groupInvocationRepository.findById(id).orElseThrow(() ->
                new GroupInvocationNotFoundException("Пришлашение в группу не найдено, id: " + id));
    }

    @Override
    @Transactional
    public Long createInvokeToGroup(Long groupId, String invitedByUsername, GroupInvocation groupInvocation) {
        groupInvocation.setStatus(GroupInvitationStatus.PENDING);
        groupInvocation.setInvitedByName(invitedByUsername);
        groupInvocation.setCreatedAt(LocalDateTime.now());
        var group = entityManager.getReference(Group.class, groupId);
        groupInvocation.setGroup(group);
        return groupInvocationRepository.save(groupInvocation).getId();
    }

    @Override
    @Transactional(readOnly = true)
    public List<GroupInvocation> getAllByUsername(String username) {
        return groupInvocationRepository.findAllByUsername(username);
    }

    @Override
    @Transactional
    public void updateInvocationStatus(Long id, GroupInvitationStatus status) throws GroupInvocationNotFoundException {
        var groupInvocation = getById(id);
        if (groupInvocation.getExpiresAt().isBefore(LocalDateTime.now())) {
            groupInvocation.setStatus(status);
        } else {
            groupInvocation.setStatus(GroupInvitationStatus.EXPIRED);
        }
    }

}
