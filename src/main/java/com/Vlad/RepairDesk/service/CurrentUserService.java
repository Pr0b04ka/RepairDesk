package com.Vlad.RepairDesk.service;

import com.Vlad.RepairDesk.model.User;
import com.Vlad.RepairDesk.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CurrentUserService {

    private final UserRepository userRepository;

    public User getCurrentUser() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        if (auth == null || !auth.isAuthenticated()) {
            throw new RuntimeException("User not authenticated");
        }

        String login = auth.getName();

        return userRepository.findByLogin(login)
                .orElseThrow(() -> new RuntimeException("User not found: " + login));
    }
}