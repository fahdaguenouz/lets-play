package com.lets_play.api.auth.controller;

import com.lets_play.api.auth.dto.AuthResponse;
import com.lets_play.api.auth.dto.LoginRequest;
import com.lets_play.api.auth.dto.RegisterRequest;
import com.lets_play.api.auth.service.AuthService;
import com.lets_play.api.users.dto.UserResponse;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/register")
    @ResponseStatus(HttpStatus.CREATED)
    public UserResponse register(@Valid @RequestBody RegisterRequest req) {
        return authService.register(req);
    }

    @PostMapping("/login")
    public AuthResponse login(@Valid @RequestBody LoginRequest req) {
        return authService.login(req);
    }

}
