package com.lets_play.api.seed;

import com.lets_play.api.users.model.Role;
import com.lets_play.api.users.model.User;
import com.lets_play.api.users.repo.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.time.Instant;

@Component
@RequiredArgsConstructor
public class AdminSeeder implements ApplicationRunner {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Value("${app.seed.admin.enabled:true}")
    private boolean enabled;

    @Value("${app.seed.admin.name:Admin}")
    private String name;

    @Value("${app.seed.admin.username:admin}")
    private String username;

    @Value("${app.seed.admin.email:admin@gmail.com}")
    private String email;

    @Value("${app.seed.admin.password:password123}")
    private String password;

    @Override
    public void run(ApplicationArguments args) {
        if (!enabled) return;

        String normalizedEmail = email.trim().toLowerCase();
        String normalizedUsername = username.trim().toLowerCase();

        boolean exists = userRepository.existsByEmail(normalizedEmail)
                || userRepository.existsByUsername(normalizedUsername);

        if (exists) return;

        Instant now = Instant.now();

        User admin = User.builder()
                .name(name.trim())
                .username(normalizedUsername)
                .email(normalizedEmail)
                .passwordHash(passwordEncoder.encode(password))
                .role(Role.ADMIN)
                .createdAt(now)
                .updatedAt(now)
                .build();

        userRepository.save(admin);

        System.out.println("✅ Admin seeded: " + normalizedEmail + " / " + normalizedUsername);
    }
}
