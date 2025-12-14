package ru.dmitriy.notificationservice.scheduler;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import ru.dmitriy.commondomain.domain.notification.GroupInvitationStatus;
import ru.dmitriy.notificationservice.repository.GroupInvocationRepository;

import java.time.LocalDateTime;

@Component
public class GroupInvocationExpirationScheduler {

    private final GroupInvocationRepository groupInvocationRepository;

    public GroupInvocationExpirationScheduler(GroupInvocationRepository groupInvocationRepository) {
        this.groupInvocationRepository = groupInvocationRepository;
    }

    @Scheduled(fixedDelayString = "${scheduled.fixedDelay.expireInvitations}")
    @Transactional
    public void expireInvitations() {
        var expiredInvitations = groupInvocationRepository.findAllExpired(LocalDateTime.now());
        expiredInvitations.forEach(inv -> inv.setStatus(GroupInvitationStatus.EXPIRED));
    }
}
