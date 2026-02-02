package com.lets_play.api.auth.dto;

import jakarta.validation.constraints.NotBlank;

public record LoginRequest(
        @NotBlank String identifier, // email OR username
        @NotBlank String password
) {}
