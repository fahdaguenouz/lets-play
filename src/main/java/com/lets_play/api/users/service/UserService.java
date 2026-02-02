package com.lets_play.api.users.service;

import com.lets_play.api.common.exception.NotFoundException;
import com.lets_play.api.users.dto.UserResponse;
import com.lets_play.api.users.repo.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    public UserResponse me() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || auth.getPrincipal() == null) {
            throw new NotFoundException("User not found");
        }

        String userId = auth.getPrincipal().toString();

        var user = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("User not found"));

        return new UserResponse(
                user.getId(),
                user.getName(),
                user.getUsername(),
                user.getEmail(),
                user.getRole(),
                user.getStatus(),
                user.getCreatedAt()
        );
    }
}
