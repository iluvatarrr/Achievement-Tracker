package ru.dmitriy.userservice.service.impl;

import org.springframework.stereotype.Service;
import ru.dmitriy.commondomain.domain.user.UserProfile;
import ru.dmitriy.userservice.repository.UserProfileRepository;
import ru.dmitriy.userservice.service.UserProfileService;

import java.util.List;

@Service
public class UserProfileServiceImpl implements UserProfileService {

    private final UserProfileRepository userProfileRepository;

    public UserProfileServiceImpl(UserProfileRepository userProfileRepository) {
        this.userProfileRepository = userProfileRepository;
    }

    @Override
    public List<UserProfile> findAll() {
        return List.of();
    }

    @Override
    public UserProfile getById(Long aLong) {
        return null;
    }

    @Override
    public Long save(UserProfile userProfile) {
        return 0L;
    }

    @Override
    public UserProfile update(Long aLong, UserProfile userProfile) {
        return null;
    }

    @Override
    public void delete(Long aLong) {

    }
}
