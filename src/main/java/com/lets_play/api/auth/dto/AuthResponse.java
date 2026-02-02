package com.lets_play.api.auth.dto;

import com.lets_play.api.users.dto.UserResponse;

public record AuthResponse(
        UserResponse user,
          String token
) {}