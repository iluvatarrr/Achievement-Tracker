package ru.dmitriy.userservice.web.security;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;
import ru.dmitriy.commondomain.domain.exception.UserNotFoundException;
import ru.dmitriy.commondomain.domain.user.User;
import ru.dmitriy.userservice.service.UserService;

@Service
public class JwtUserDetailsService implements UserDetailsService {

    private final UserService userService;

    public JwtUserDetailsService(UserService userService) {
        this.userService = userService;
    }

    @Override
    public UserDetails loadUserByUsername(String username) {
        User user = null;
        try {
            user = userService.getByUsername(username);
        } catch (UserNotFoundException e) {
            throw new RuntimeException(e);
        }
        return JwtEntityFactory.createJwtEntity(user);
    }
}
