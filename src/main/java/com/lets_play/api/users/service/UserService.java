package com.lets_play.api.users.service;

import com.lets_play.api.common.exception.ConflictException;
import com.lets_play.api.common.exception.NotFoundException;
import com.lets_play.api.common.exception.UnauthorizedException;
import com.lets_play.api.products.repo.ProductRepository;
import com.lets_play.api.security.AuthPrincipal;
import com.lets_play.api.users.dto.UserCreateRequest;
import com.lets_play.api.users.dto.UserResponse;
import com.lets_play.api.users.dto.UserUpdateRequest;
import com.lets_play.api.users.model.Role;
import com.lets_play.api.users.model.User;
import com.lets_play.api.users.repo.UserRepository;

import lombok.RequiredArgsConstructor;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final ProductRepository productRepository;

    public List<UserResponse> listAll() {
        return userRepository.findAll().stream().map(this::toResponse).toList();
    }

    public UserResponse getById(String id) {
        User u = userRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("User not found"));
        return toResponse(u);
    }

    public UserResponse create(UserCreateRequest req) {
        String email = req.email().trim().toLowerCase();
        String username = req.username().trim().toLowerCase();

        if (userRepository.existsByEmail(email))
            throw new ConflictException("Email already in use");
        if (userRepository.existsByUsername(username))
            throw new ConflictException("Username already in use");

        Instant now = Instant.now();

        User u = User.builder()
                .name(req.name().trim())
                .email(email)
                .username(username)
                .passwordHash(passwordEncoder.encode(req.password()))
                .role(req.role() == null ? Role.USER : req.role())
                .createdAt(now)
                .updatedAt(now)
                .build();

        return toResponse(userRepository.save(u));
    }

    public UserResponse update(String id, UserUpdateRequest req) {
        User u = userRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("User not found"));

        if (req.email() != null) {
            String email = req.email().trim().toLowerCase();
            if (!email.equals(u.getEmail()) && userRepository.existsByEmail(email)) {
                throw new ConflictException("Email already in use");
            }
            u.setEmail(email);
        }

        if (req.username() != null) {
            String username = req.username().trim().toLowerCase();
            if (!username.equals(u.getUsername()) && userRepository.existsByUsername(username)) {
                throw new ConflictException("Username already in use");
            }
            u.setUsername(username);
        }

        if (req.name() != null)
            u.setName(req.name().trim());
        if (req.password() != null)
            u.setPasswordHash(passwordEncoder.encode(req.password()));
        if (req.role() != null)
            u.setRole(req.role());

        u.setUpdatedAt(Instant.now());
        return toResponse(userRepository.save(u));
    }

    public void delete(String id) {
        User u = userRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("User not found"));

        productRepository.deleteAllByUserId(id); // delete owned products
        userRepository.delete(u);
    }

    public UserResponse me() {
        var auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || auth.getPrincipal() == null) {
            throw new UnauthorizedException("Unauthorized");
        }

        String userId;

        Object principal = auth.getPrincipal();
        if (principal instanceof AuthPrincipal ap) {
            userId = ap.userId();
        } else {
            // fallback (in case something sets principal as String)
            userId = principal.toString();
        }

        var user = userRepository.findById(userId)
                .orElseThrow(() -> new UnauthorizedException("Unauthorized"));

        return toResponse(user);
    }

    private UserResponse toResponse(User user) {
        return new UserResponse(
                user.getId(),
                user.getName(),
                user.getUsername(),
                user.getEmail(),
                user.getRole(),
                user.getCreatedAt());
    }
}
