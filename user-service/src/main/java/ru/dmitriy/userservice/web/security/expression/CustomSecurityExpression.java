package ru.dmitriy.userservice.web.security.expression;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import ru.dmitriy.commondomain.domain.group.GroupRole;
import ru.dmitriy.commondomain.domain.user.Role;
import ru.dmitriy.userservice.service.UserService;
import ru.dmitriy.userservice.web.security.JwtEntity;

import java.util.Set;

@Service("customSecurityExpression")
public class CustomSecurityExpression {

    private final UserService userService;
    public CustomSecurityExpression(UserService userService) {
        this.userService = userService;
    }

    public boolean canAccessUser(Long id) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        var user = (JwtEntity) authentication.getPrincipal();
        Long userId = user.getId();
        return (userId.equals(id) || hasAnyRole(authentication, Role.ROLE_ADMIN));
    }

    public boolean canAccessGroup(Long groupId) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        var user = (JwtEntity) authentication.getPrincipal();
        Long userId = user.getId();
        boolean isMemberOrMore = userService.checkAccessByGroupRoles(userId, groupId,
                Set.of(GroupRole.OWNER, GroupRole.MEMBER, GroupRole.MODERATOR));
        return isMemberOrMore || hasAnyRole(authentication, Role.ROLE_ADMIN);
    }

    public boolean canAccessGoal(Long goalId) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        var user = (JwtEntity) authentication.getPrincipal();
        Long userId = user.getId();
        boolean isContains = userService.containsGoalWithId(userId, goalId);
        return isContains || hasAnyRole(authentication, Role.ROLE_ADMIN);
    }

    public boolean canAccessSubGoal(Long subGoalId) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        var user = (JwtEntity) authentication.getPrincipal();
        Long userId = user.getId();
        boolean isContains = userService.containsSubGoalWithId(userId, subGoalId);
        return isContains || hasAnyRole(authentication, Role.ROLE_ADMIN);
    }

    public boolean isOwnerGroup(Long groupId) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        var user = (JwtEntity) authentication.getPrincipal();
        Long userId = user.getId();
        boolean isOwner = userService.checkAccessByGroupRoles(userId, groupId, Set.of(GroupRole.OWNER));
        return isOwner || hasAnyRole(authentication, Role.ROLE_ADMIN);
    }

    public boolean canManageMember(Long groupId) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        var user = (JwtEntity) authentication.getPrincipal();
        Long userId = user.getId();
        boolean isManagerMember = userService.checkAccessByGroupRoles(userId, groupId, Set.of(GroupRole.OWNER, GroupRole.MODERATOR));
        return isManagerMember || hasAnyRole(authentication, Role.ROLE_ADMIN);
    }

    private boolean hasAnyRole(Authentication authentication, Role... roles) {
        for (Role role : roles) {
            SimpleGrantedAuthority authority = new SimpleGrantedAuthority(role.name());
            if (authentication.getAuthorities().contains(authority)) {
                return true;
            }
        }
        return false;
    }
}
