package ru.dmitriy.commondomain.listener.event;

import ru.dmitriy.commondomain.domain.group.Group;
import ru.dmitriy.commondomain.domain.notification.GroupInvitationStatus;

public record GroupInvitationUpdatedEvent(
        Long invitationId,
        GroupInvitationStatus status,
        String username,
        Group group
) {
}
