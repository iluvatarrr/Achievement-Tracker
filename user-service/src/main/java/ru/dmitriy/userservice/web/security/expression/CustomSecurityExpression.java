package ru.dmitriy.userservice.web.security.expression;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import ru.dmitriy.commondomain.domain.group.GroupRole;
import ru.dmitriy.commondomain.domain.user.Role;
import ru.dmitriy.userservice.config.CurrentUserProvider;
import ru.dmitriy.userservice.service.UserService;
import ru.dmitriy.userservice.web.security.JwtEntity;

import java.util.Set;

@Service("customSecurityExpression")
public class CustomSecurityExpression {

    private final UserService userService;
    private final CurrentUserProvider currentUserProvider;
    public CustomSecurityExpression(UserService userService, CurrentUserProvider currentUserProvider) {
        this.userService = userService;
        this.currentUserProvider = currentUserProvider;
    }

    public boolean canAccessUser(Long id) {
        Long userId = currentUserProvider.getCurrentUserId();
        return (userId.equals(id) || hasAnyRole(Role.ROLE_ADMIN));
    }

    public boolean canAccessUsername(String username) {
        String currentUsername = currentUserProvider.getCurrentUsername();
        return (currentUsername.equals(username) || hasAnyRole(Role.ROLE_ADMIN));
    }

    public boolean canAccessGroup(Long groupId) {
        Long userId = currentUserProvider.getCurrentUserId();
        boolean isMemberOrMore = userService.checkAccessByGroupRoles(userId, groupId,
                Set.of(GroupRole.OWNER, GroupRole.MEMBER, GroupRole.MODERATOR));
        return isMemberOrMore || hasAnyRole(Role.ROLE_ADMIN);
    }

    public boolean canAccessGoal(Long goalId) {
        Long userId = currentUserProvider.getCurrentUserId();
        boolean isContains = userService.containsGoalWithId(userId, goalId);
        return isContains || hasAnyRole(Role.ROLE_ADMIN);
    }

    public boolean canAccessSubGoal(Long subGoalId) {
        Long userId = currentUserProvider.getCurrentUserId();
        boolean isContains = userService.containsSubGoalWithId(userId, subGoalId);
        return isContains || hasAnyRole(Role.ROLE_ADMIN);
    }

    public boolean isOwnerGroup(Long groupId) {
        Long userId = currentUserProvider.getCurrentUserId();
        boolean isOwner = userService.checkAccessByGroupRoles(userId, groupId, Set.of(GroupRole.OWNER));
        return isOwner || hasAnyRole(Role.ROLE_ADMIN);
    }

    public boolean canManageMember(Long groupId) {
        Long userId = currentUserProvider.getCurrentUserId();
        boolean isManagerMember = userService.checkAccessByGroupRoles(userId, groupId, Set.of(GroupRole.OWNER, GroupRole.MODERATOR));
        return isManagerMember || hasAnyRole(Role.ROLE_ADMIN);
    }

    public boolean canInvoke(Long groupId, String username) {
        String currentUsername = currentUserProvider.getCurrentUsername();
        Long userId = currentUserProvider.getCurrentUserId();
        boolean isManagerMember = userService.checkAccessByGroupRoles(userId, groupId, Set.of(GroupRole.OWNER, GroupRole.MODERATOR));
        return (isManagerMember && currentUsername.equals(username)) || hasAnyRole(Role.ROLE_ADMIN);
    }

    public boolean canAccessInvocation(Long invocationId) {
        String currentUsername = currentUserProvider.getCurrentUsername();
        Long userId = currentUserProvider.getCurrentUserId();
        boolean isAccessInvoke = userService.checkAccessInvocation(invocationId, userId);
        return isAccessInvoke || hasAnyRole(Role.ROLE_ADMIN);
    }

    private boolean hasAnyRole(Role... roles) {
        for (Role role : roles) {
            SimpleGrantedAuthority authority = new SimpleGrantedAuthority(role.name());
            if (currentUserProvider.getCurrentGrantedAuthority().contains(authority)) {
                return true;
            }
        }
        return false;
    }
}
