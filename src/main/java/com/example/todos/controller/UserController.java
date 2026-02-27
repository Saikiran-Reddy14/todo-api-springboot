package com.example.todos.controller;

import java.time.LocalDateTime;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.todos.dto.ApiResponse;
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
        return ResponseEntity.ok(ApiResponse.<String>builder()
                .message("User registered successfully")
                .status(200)
                .data(null)
                .timestamp(LocalDateTime.now())
                .build());
    }

}
