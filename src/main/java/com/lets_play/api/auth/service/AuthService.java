package com.lets_play.api.auth.service;

import com.lets_play.api.auth.dto.AuthResponse;
import com.lets_play.api.auth.dto.LoginRequest;
import com.lets_play.api.auth.dto.RegisterRequest;
import com.lets_play.api.common.exception.ConflictException;
import com.lets_play.api.common.exception.UnauthorizedException;
import com.lets_play.api.security.JwtService;
import com.lets_play.api.users.dto.UserResponse;
import com.lets_play.api.users.model.Role;
import com.lets_play.api.users.model.User;
import com.lets_play.api.users.repo.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    public UserResponse register(RegisterRequest req) {
        String email = req.email().trim().toLowerCase();
        String username = req.username().trim().toLowerCase();

        if (userRepository.existsByEmail(email)) throw new ConflictException("Email already in use");
        if (userRepository.existsByUsername(username)) throw new ConflictException("Username already in use");

        Instant now = Instant.now();

        User user = User.builder()
                .name(req.name().trim())
                .email(email)
                .username(username)
                .passwordHash(passwordEncoder.encode(req.password()))
                .role(Role.USER)
                .createdAt(now)
                .updatedAt(now)
                .build();

        User saved = userRepository.save(user);

        return toUserResponse(saved);
    }

    public AuthResponse login(LoginRequest req) {
        String id = req.identifier().trim().toLowerCase();

        User user = userRepository.findByEmail(id)
                .or(() -> userRepository.findByUsername(id))
                .orElseThrow(() -> new UnauthorizedException("Invalid credentials"));

        if (!passwordEncoder.matches(req.password(), user.getPasswordHash())) {
            throw new UnauthorizedException("Invalid credentials");
        }

        String token = jwtService.generateToken(
                user.getId(),
                user.getUsername(),
                List.of(new SimpleGrantedAuthority("ROLE_" + user.getRole().name()))
        );

        return new AuthResponse(toUserResponse(user), token);
    }

    private UserResponse toUserResponse(User u) {
        return new UserResponse(
                u.getId(),
                u.getName(),
                u.getUsername(),
                u.getEmail(),
                u.getRole(),
                u.getCreatedAt()
        );
    }
}
