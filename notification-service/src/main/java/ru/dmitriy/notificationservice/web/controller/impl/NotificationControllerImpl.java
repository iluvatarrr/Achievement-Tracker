package ru.dmitriy.notificationservice.web.controller.impl;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.dmitriy.commondomain.domain.exception.GroupInvocationNotFoundException;
import ru.dmitriy.commondomain.domain.notification.GroupInvitationStatus;
import ru.dmitriy.commondomain.domain.notification.GroupInvocation;
import ru.dmitriy.commondomain.util.Mappable;
import ru.dmitriy.commondomain.util.MapperRegistry;
import ru.dmitriy.notificationservice.service.NotificationService;
import ru.dmitriy.notificationservice.web.controller.NotificationController;
import ru.dmitriy.notificationservice.web.dto.CreateGroupInvocationDto;
import ru.dmitriy.notificationservice.web.dto.GroupInvocationDto;

import java.util.List;

@Validated
@RestController
@RequestMapping("/api/v1/notify")
public class NotificationControllerImpl implements NotificationController {

    private final NotificationService notificationService;
    private final MapperRegistry mapperRegistry;

    public NotificationControllerImpl(NotificationService notificationService, MapperRegistry mapperRegistry) {
        this.notificationService = notificationService;
        this.mapperRegistry = mapperRegistry;
    }

    @Override
    @PostMapping("/invoke/{groupId}")
    @PreAuthorize("@customSecurityExpression.canInvoke(#groupId, #invokeByUsername)")
    public Long invokeToGroup(@Min(1) @PathVariable Long groupId, @NotBlank @RequestParam String invokeByUsername, @Valid @RequestBody CreateGroupInvocationDto createGroupInvocationDto) {
        Mappable<GroupInvocation, CreateGroupInvocationDto> mapper = mapperRegistry.get("createGroupInvocationDtoMapper");
        var groupInvocation = mapper.toEntity(createGroupInvocationDto);
        return notificationService.createInvokeToGroup(groupId, invokeByUsername, groupInvocation);
    }

    @Override
    @GetMapping("/all/invocation")
    @PreAuthorize("@customSecurityExpression.canAccessUsername(#username)")
    public List<GroupInvocationDto> getGroupInvocations(@NotBlank @RequestParam String username) {
        Mappable<GroupInvocation, GroupInvocationDto> mapper = mapperRegistry.get("groupInvocationDtoMapper");
        var groupInvocations = notificationService.getAllByUsername(username);
        return mapper.toDto(groupInvocations);
    }

    @Override
    @PatchMapping("/change/{id}/status")
    @PreAuthorize("@customSecurityExpression.canAccessInvocation(#id)")
    public void updateInvocationStatus(@Min(1) @PathVariable Long id, @NotNull @RequestParam GroupInvitationStatus status) throws GroupInvocationNotFoundException {
        notificationService.updateInvocationStatus(id, status);
    }

}
