package ru.dmitriy.userservice.service;

import org.springframework.http.ResponseEntity;
import ru.dmitriy.commondomain.domain.group.GroupRole;
import ru.dmitriy.commondomain.domain.user.User;
import ru.dmitriy.commondomain.domain.exception.UserNotFoundException;
import ru.dmitriy.userservice.web.dto.user.ChangePasswordRequest;

import java.util.List;
import java.util.Set;

public interface UserService extends CRUDService<Long, User> {
    User updateWithoutProfile(Long id, User user) throws UserNotFoundException;
    User getByUsername(String username) throws UserNotFoundException;
    ResponseEntity<?> changePassword(ChangePasswordRequest req) throws UserNotFoundException;
    boolean containsGoalWithId(Long userId, Long goalId);
    boolean containsSubGoalWithId(Long userId, Long goalId);
    boolean checkAccessByGroupRoles(Long userId,Long groupId, Set<GroupRole> groupRoles);
}
