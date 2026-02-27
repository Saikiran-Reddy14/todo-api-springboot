package com.example.todos.service;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.todos.dto.LoginReq;
import com.example.todos.dto.LoginRes;
import com.example.todos.dto.RegisterReq;
import com.example.todos.entity.RefreshToken;
import com.example.todos.entity.User;
import com.example.todos.exception.ResourceExists;
import com.example.todos.exception.ResourceNotFound;
import com.example.todos.repo.RefreshTokenRepo;
import com.example.todos.repo.UserRepo;
import com.example.todos.utils.JwtUtils;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepo userRepo;
    private final RefreshTokenRepo refreshTokenRepo;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtils jwtUtils;
    private final AuthenticationManager authenticationManager;

    @Value("${jwt.refreshTokenExpiration}")
    private long refreshTokenExpiration;

    public void registerUser(RegisterReq request) {
        if (userRepo.existsByEmail(request.email())) {
            throw new ResourceExists("Email already exists");
        }

        if (userRepo.existsByUsername(request.username())) {
            throw new ResourceExists("Username already exists");
        }
        User user = User.builder()
                .email(request.email())
                .password(passwordEncoder.encode(request.password()))
                .username(request.username())
                .build();
        userRepo.save(user);
    }

    @Transactional
    public LoginRes loginUser(LoginReq request) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.username(), request.password()));

        String username = authentication.getName();
        String accessToken = jwtUtils.generateAccessToken(username);
        String refreshToken = jwtUtils.generateRefreshToken(username);

        User user = userRepo.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFound("User not found"));
        refreshTokenRepo.deleteByUser(user);

        RefreshToken storedToken = RefreshToken.builder()
                .token(refreshToken)
                .user(user)
                .expiryDate(LocalDateTime.now().plus(refreshTokenExpiration, ChronoUnit.MILLIS))
                .build();
        refreshTokenRepo.save(storedToken);

        return LoginRes.builder().accessToken(accessToken).refreshToken(refreshToken).build();
    }

    @Transactional
    public LoginRes refreshTokens(String refreshToken) {

        String username = jwtUtils.extractUsername(refreshToken);

        RefreshToken storedToken = refreshTokenRepo.findByToken(refreshToken)
                .orElseThrow(() -> new ResourceNotFound("Invalid refresh token"));

        if (!username.equals(storedToken.getUser().getUsername())) {
            refreshTokenRepo.delete(storedToken);
            throw new ResourceNotFound("Token mismatch. Please login again");
        }

        if (storedToken.isExpired()) {
            refreshTokenRepo.delete(storedToken);
            throw new ResourceNotFound("Refresh token has expired. Please login again");
        }

        User user = storedToken.getUser();
        refreshTokenRepo.delete(storedToken);

        String newAccessToken = jwtUtils.generateAccessToken(user.getUsername());
        String newRefreshToken = jwtUtils.generateRefreshToken(user.getUsername());

        RefreshToken newStoredToken = RefreshToken.builder()
                .token(newRefreshToken)
                .user(user)
                .expiryDate(LocalDateTime.now().plus(refreshTokenExpiration, ChronoUnit.MILLIS))
                .build();
        refreshTokenRepo.save(newStoredToken);

        return LoginRes.builder().accessToken(newAccessToken).refreshToken(newRefreshToken).build();
    }

    @Transactional
    public void logoutUser(String username) {
        User user = userRepo.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFound("User not found"));
        refreshTokenRepo.deleteByUser(user);
    }

}
