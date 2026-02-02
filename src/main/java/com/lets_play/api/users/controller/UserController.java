package com.lets_play.api.users.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.lets_play.api.users.dto.UserResponse;
import com.lets_play.api.users.service.UserService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService; 

    @GetMapping("/me")
    public UserResponse me() {
        return userService.me();
    }
}
