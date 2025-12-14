package ru.dmitriy.notificationservice.web.controller;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import ru.dmitriy.commondomain.domain.exception.GroupInvocationNotFoundException;
import ru.dmitriy.commondomain.domain.notification.GroupInvitationStatus;
import ru.dmitriy.notificationservice.web.dto.CreateGroupInvocationDto;
import ru.dmitriy.notificationservice.web.dto.GroupInvocationDto;
import java.util.List;

public interface NotificationController {
    void updateInvocationStatus(@Min(1) @PathVariable Long id, @NotNull @RequestParam GroupInvitationStatus status) throws GroupInvocationNotFoundException;
    List<GroupInvocationDto> getGroupInvocations(@NotBlank String username);
    Long invokeToGroup(@Min(1) Long groupId, @NotBlank String invokeByUsername, @Valid CreateGroupInvocationDto createGroupInvocationDto);
}
