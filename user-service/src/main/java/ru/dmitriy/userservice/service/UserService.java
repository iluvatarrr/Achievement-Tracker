package ru.dmitriy.userservice.service;

import org.springframework.http.ResponseEntity;
import ru.dmitriy.commondomain.domain.group.GroupRole;
import ru.dmitriy.commondomain.domain.user.Role;
import ru.dmitriy.commondomain.domain.user.User;
import ru.dmitriy.commondomain.domain.exception.UserNotFoundException;
import ru.dmitriy.commondomain.domain.user.UserStatus;
import ru.dmitriy.userservice.web.dto.user.ChangePasswordRequest;
import java.util.Set;

public interface UserService extends CRUDService<Long, User> {
    User setUserStatus(Long id, UserStatus status) throws UserNotFoundException;
    User addRoleById(Long id, Role role) throws UserNotFoundException;
    User updateWithoutProfile(Long id, User user) throws UserNotFoundException;
    User getByUsername(String username) throws UserNotFoundException;
    ResponseEntity<?> changePassword(ChangePasswordRequest req) throws UserNotFoundException;
    boolean containsGoalWithId(Long userId, Long goalId);
    boolean containsSubGoalWithId(Long userId, Long goalId);
    boolean checkAccessByGroupRoles(Long userId,Long groupId, Set<GroupRole> groupRoles);
    boolean checkAccessInvocation(Long invocationId, Long userId);
}
