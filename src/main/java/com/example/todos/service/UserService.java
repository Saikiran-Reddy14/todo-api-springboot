package com.example.todos.service;

import java.util.Optional;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.todos.dto.RegisterReq;
import com.example.todos.entity.User;
import com.example.todos.repo.UserRepo;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepo userRepo;
    private final PasswordEncoder passwordEncoder;

    public void registerUser(RegisterReq request) {
        if (userRepo.existsByEmail(request.email())) {
            throw new RuntimeException("Email already exists");
        }

        if (userRepo.existsByUsername(request.username())) {
            throw new RuntimeException("Username already exists");
        }
        User user = User.builder()
                .email(request.email())
                .password(passwordEncoder.encode(request.password()))
                .username(request.username())
                .build();
        userRepo.save(user);
    }

}
