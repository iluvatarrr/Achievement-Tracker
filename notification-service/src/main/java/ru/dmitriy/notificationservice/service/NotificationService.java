package ru.dmitriy.notificationservice.service;

import ru.dmitriy.commondomain.domain.exception.GroupInvocationNotFoundException;
import ru.dmitriy.commondomain.domain.notification.GroupInvitationStatus;
import ru.dmitriy.commondomain.domain.notification.GroupInvocation;

import java.util.List;

public interface NotificationService {
    GroupInvocation getById(Long id) throws GroupInvocationNotFoundException;
    Long createInvokeToGroup(Long groupId, String username, GroupInvocation invocation);
    List<GroupInvocation> getAllByUsername(String username);
    void updateInvocationStatus(Long id, GroupInvitationStatus status) throws GroupInvocationNotFoundException;
}
