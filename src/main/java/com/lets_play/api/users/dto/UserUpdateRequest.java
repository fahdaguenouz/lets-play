package com.lets_play.api.users.dto;


import jakarta.validation.constraints.Size;

public record UserUpdateRequest(
        @Size(min = 3, max = 50) String name,
        @Size(min = 3, max = 30) String username
) {}
