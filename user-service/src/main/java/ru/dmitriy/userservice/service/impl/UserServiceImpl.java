package ru.dmitriy.userservice.service.impl;

import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.dmitriy.commondomain.domain.group.GroupRole;
import ru.dmitriy.commondomain.domain.user.Role;
import ru.dmitriy.commondomain.domain.user.User;
import ru.dmitriy.commondomain.domain.user.UserProfile;
import ru.dmitriy.commondomain.domain.exception.UserNotFoundException;
import ru.dmitriy.commondomain.domain.user.UserStatus;
import ru.dmitriy.userservice.config.CurrentUserProvider;
import ru.dmitriy.userservice.repository.UserRepository;
import ru.dmitriy.userservice.service.UserService;
import ru.dmitriy.userservice.web.dto.user.ChangePasswordRequest;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

@Service
public class UserServiceImpl implements UserService {

    private final CurrentUserProvider currentUser;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    public UserServiceImpl(CurrentUserProvider currentUser, UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.currentUser = currentUser;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    @Transactional(readOnly = true)
    public List<User> findAll() {
        return userRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public User getByUsername(String username) throws UserNotFoundException {
        return userRepository.findByUsername(username).orElseThrow(() ->
                new UserNotFoundException("Пользователь не найлен по usename: " + username));
    }

    @Override
    @Transactional(readOnly = true)
    public User getById(Long id) throws UserNotFoundException {
        return userRepository.findById(id)
                .orElseThrow(()-> new UserNotFoundException("Пользователь не найден с id: " + id));
    }

    @Override
    @Transactional
    public Long save(User user) {
        user.setCreatedAt(LocalDateTime.now());
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setRoles(Set.of(Role.ROLE_USER));
        user.setUserStatus(UserStatus.ACTIVE);
        return userRepository.save(user).getId();
    }

    @Override
    @Transactional
    public User update(Long id, User userToUpdate) throws UserNotFoundException {
        User existingUser = getById(id);
        existingUser.setUsername(userToUpdate.getUsername());
        existingUser.setRoles(userToUpdate.getRoles());
        existingUser.setUserStatus(userToUpdate.getUserStatus());
        if (userToUpdate.getProfile() != null) {
            UserProfile newProfile = userToUpdate.getProfile();
            if (existingUser.getProfile() != null) {
                newProfile.setUserId(existingUser.getProfile().getUserId());
                newProfile.setUser(existingUser);
            } else {
                newProfile.setUser(existingUser);
            }
            existingUser.setProfile(newProfile);
        } else if (existingUser.getProfile() != null) {
            existingUser.getProfile().setUser(null);
            existingUser.setProfile(null);
        }
        return userRepository.save(existingUser);
    }

    @Override
    @Transactional
    public ResponseEntity<?> changePassword(ChangePasswordRequest req) throws UserNotFoundException {
        var id = currentUser.getCurrentUserId();
        var user = getById(id);
        if (req.currentPassword() == null
                || !passwordEncoder.matches(req.currentPassword(), user.getPassword())) {
            return ResponseEntity.status(401).build();
        }
        if (req.newPassword() == null
                || req.newPassword().isBlank()) {
            return ResponseEntity.badRequest().build();
        }
        user.setPassword(passwordEncoder.encode(req.newPassword()));
        userRepository.save(user);
        return ResponseEntity.ok().build();
    }

    @Override
    @Transactional(readOnly = true)
    public boolean containsGoalWithId(Long userId, Long goalId) {
        return userRepository.userHasGoal(userId,goalId);
    }

    @Override
    @Transactional(readOnly = true)
    public boolean containsSubGoalWithId(Long userId, Long subGoalId) {
        return userRepository.userHasSubGoal(userId, subGoalId);
    }

    @Override
    @Transactional(readOnly = true)
    public boolean checkAccessByGroupRoles(Long userId, Long groupId, Set<GroupRole> groupRoles) {
        return userRepository.checkAccessByGroupRoles(userId, groupId, groupRoles);
    }


    @Override
    @Transactional
    public User updateWithoutProfile(Long id, User userToUpdate) throws UserNotFoundException {
        var user = getById(id);
        userToUpdate.setProfile(user.getProfile());
        return userRepository.save(userToUpdate);
    }

    @Override
    @Transactional
    public void delete(Long id) {
        userRepository.deleteById(id);
    }
}
