package com.example.todos.controller;

import java.time.LocalDateTime;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.todos.dto.ApiResponse;
import com.example.todos.dto.LoginReq;
import com.example.todos.dto.LoginRes;
import com.example.todos.dto.RefreshTokenReq;
import com.example.todos.dto.RegisterReq;
import com.example.todos.service.UserService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PostMapping("/register")
    public ResponseEntity<ApiResponse<String>> register(@RequestBody @Valid RegisterReq request) {
        userService.registerUser(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.<String>builder()
                .message("User registered successfully")
                .status(201)
                .data(null)
                .timestamp(LocalDateTime.now())
                .build());
    }

    @PostMapping("/login")
    public ResponseEntity<ApiResponse<LoginRes>> login(@RequestBody @Valid LoginReq request) {
        LoginRes tokens = userService.loginUser(request);
        return ResponseEntity.ok(ApiResponse.<LoginRes>builder()
                .message("Login successful")
                .status(200)
                .data(tokens)
                .timestamp(LocalDateTime.now())
                .build());
    }

    @PostMapping("/refresh")
    public ResponseEntity<ApiResponse<LoginRes>> refresh(@RequestBody @Valid RefreshTokenReq request) {
        LoginRes tokens = userService.refreshTokens(request.refreshToken());
        return ResponseEntity.ok(ApiResponse.<LoginRes>builder()
                .message("Tokens refreshed successfully")
                .status(200)
                .data(tokens)
                .timestamp(LocalDateTime.now())
                .build());
    }

    @PostMapping("/logout")
    public ResponseEntity<ApiResponse<String>> logout(Authentication authentication) {
        userService.logoutUser(authentication.getName());
        return ResponseEntity.ok(ApiResponse.<String>builder()
                .message("Logged out successfully")
                .status(200)
                .data(null)
                .timestamp(LocalDateTime.now())
                .build());
    }

}
