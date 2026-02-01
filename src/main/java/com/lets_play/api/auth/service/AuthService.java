package com.lets_play.api.auth.service;



import com.lets_play.api.auth.dto.AuthResponse;
import com.lets_play.api.auth.dto.RegisterRequest;
import com.lets_play.api.common.exception.ConflictException;

import com.lets_play.api.users.dto.UserResponse;
import com.lets_play.api.users.model.Role;
import com.lets_play.api.users.model.User;
import com.lets_play.api.users.repo.UserRepository;

import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.Instant;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;

    // For now create it here; later we’ll move to @Bean in SecurityConfig
    private final PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    public AuthResponse register(RegisterRequest req) {
        String email = req.email().trim().toLowerCase();
        String username = req.username().trim().toLowerCase();

        if (userRepository.existsByEmail(email)) {
            throw new ConflictException("Email already in use");
        }
        if (userRepository.existsByUsername(username)) {
            throw new ConflictException("Username already in use");
        }

        Instant now = Instant.now();

        User user = User.builder()
                .name(req.name().trim())
                .email(email)
                .username(username)
                .passwordHash(passwordEncoder.encode(req.password()))
                .role(Role.USER)
                .status("ACTIVE")
                .createdAt(now)
                .updatedAt(now)
                .build();

        User saved = userRepository.save(user);

        UserResponse safe = new UserResponse(
                saved.getId(),
                saved.getName(),
                saved.getUsername(),
                saved.getEmail(),
                saved.getRole(),
                saved.getStatus(),
                saved.getCreatedAt()
        );

        return new AuthResponse(safe);
    }
}
