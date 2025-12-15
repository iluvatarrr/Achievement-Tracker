package ru.dmitriy.groupservice.listner;

import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;
import ru.dmitriy.commondomain.domain.exception.GroupNotFoundException;
import ru.dmitriy.commondomain.domain.exception.UserNotFoundException;
import ru.dmitriy.commondomain.domain.notification.GroupInvitationStatus;
import ru.dmitriy.commondomain.domain.notification.GroupInvocation;
import ru.dmitriy.commondomain.domain.user.User;
import ru.dmitriy.commondomain.listener.event.GroupInvitationUpdatedEvent;
import ru.dmitriy.groupservice.service.GroupService;
import ru.dmitriy.userservice.service.UserService;

@Service
public class GroupInvitationEventListener {

    private final GroupService groupService;
    private final UserService userService;
    public GroupInvitationEventListener(GroupService groupService, UserService userService) {
        this.groupService = groupService;
        this.userService = userService;
    }

    @EventListener
    @Transactional
    public void handleGroupInvitationUpdated(GroupInvitationUpdatedEvent event) {
        if (event.status() != GroupInvitationStatus.ACCEPTED) {
            return;
        }
        Long invitationId = event.invitationId();
        var group = event.group();
        var isContainInvocation = group.getInvocations()
                .stream()
                .map(GroupInvocation::getId)
                .toList()
                .contains(invitationId);
        User user = null;
        try {
            user = userService.getByUsername(event.username());
        } catch (UserNotFoundException e) {
            throw new RuntimeException("Пользователь не найден", e);
        }
        if (user != null && isContainInvocation) {
            try {
                groupService.addMember(group.getId(), user.getId());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}