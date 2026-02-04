package com.lets_play.api.users.dto;

import com.lets_play.api.users.model.Role;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;

public record UserUpdateRequest(
        @Size(min = 3, max = 50) String name,
        @Size(min = 3, max = 30) String username,
        @Email String email,
        @Size(min = 8, max = 72) String password,
        Role role,
        String status
) {}
