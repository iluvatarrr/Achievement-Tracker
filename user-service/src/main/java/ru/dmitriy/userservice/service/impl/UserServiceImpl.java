package ru.dmitriy.userservice.service.impl;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.dmitriy.commondomain.domain.user.User;
import ru.dmitriy.commondomain.domain.user.UserProfile;
import ru.dmitriy.commondomain.domain.exception.UserNotFoundException;
import ru.dmitriy.userservice.repository.UserRepository;
import ru.dmitriy.userservice.service.UserService;
import java.util.List;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public List<User> findAll() {
        return userRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public User getById(Long id) throws UserNotFoundException {
        return userRepository.findById(id)
                .orElseThrow(()-> new UserNotFoundException("User not found with id: " + id));
    }

    @Override
    @Transactional
    public Long save(User user) {
        return userRepository.save(user).getId();
    }

    @Override
    @Transactional
    public User update(Long id, User userToUpdate) throws UserNotFoundException {
        User existingUser = getById(id);
        existingUser.setEmail(userToUpdate.getEmail());
        existingUser.setPassword(userToUpdate.getPassword());
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
