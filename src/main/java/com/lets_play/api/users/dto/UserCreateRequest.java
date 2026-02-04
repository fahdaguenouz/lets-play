package com.lets_play.api.users.dto;

import com.lets_play.api.users.model.Role;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record UserCreateRequest(
        @NotBlank @Size(min = 3, max = 50) String name,
        @NotBlank @Size(min = 3, max = 30) String username,
        @NotBlank @Email String email,
        @NotBlank @Size(min = 8, max = 72) String password,
        Role role // optional (if null -> USER)
) {}
