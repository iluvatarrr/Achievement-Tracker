package ru.dmitriy.userservice.config;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import ru.dmitriy.userservice.web.security.JwtEntity;

import java.util.Collection;

@Component
public class CurrentUserProvider {

    public Long getCurrentUserId() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null && auth.isAuthenticated() && !"anonymousUser".equals(auth.getPrincipal())) {
            var userDetails = (JwtEntity) auth.getPrincipal();
            return userDetails.getId();
        }
        throw new IllegalStateException("No authenticated user found");
    }

    public String getCurrentUsername() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null && auth.isAuthenticated() && !"anonymousUser".equals(auth.getPrincipal())) {
            var userDetails = (JwtEntity) auth.getPrincipal();
            return userDetails.getUsername();
        }
        throw new IllegalStateException("No authenticated user found");
    }

    public Collection<? extends GrantedAuthority> getCurrentGrantedAuthority() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null && auth.isAuthenticated() && !"anonymousUser".equals(auth.getPrincipal())) {
            var userDetails = (JwtEntity) auth.getPrincipal();
            return userDetails.getAuthorities();
        }
        throw new IllegalStateException("No authenticated user found");
    }
}

