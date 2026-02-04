package com.lets_play.api.users.dto;




import java.time.Instant;

import com.lets_play.api.users.model.Role;

public record UserResponse(
        String id,
        String name,
        String username,
        String email,
        Role role,
        Instant createdAt
) {}
