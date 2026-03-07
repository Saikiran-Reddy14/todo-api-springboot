package com.example.todos.utils;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Date;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;

@Component
public class JwtUtils {

    @Value("${jwt.secret}")
    private String secret;

    @Value("${jwt.accessTokenExpiration}")
    private long accessTokenExpiration;

    @Value("${jwt.refreshTokenExpiration}")
    private long refreshTokenExpiration;

    private Key getSignKey() {
        return Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
    }

    public String generateAccessToken(String username) {
        return buildToken(username, accessTokenExpiration, "ACCESS");
    }

    public String generateRefreshToken(String username) {
        return buildToken(username, refreshTokenExpiration, "REFRESH");
    }

    private String buildToken(String username, long expiration, String tokenType) {
        return Jwts.builder()
                .subject(username)
                .claim("type", tokenType)
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + expiration))
                .signWith(getSignKey())
                .compact();
    }

    public String extractUsername(String token) {
        return extractAllClaims(token).getSubject();
    }

    public boolean validateToken(String token, UserDetails userDetails) {
        String username = extractUsername(token);
        String tokenType = extractAllClaims(token).get("type", String.class);
        return username.equals(userDetails.getUsername()) && !isTokenExpired(token) && "ACCESS".equals(tokenType);
    }

    public boolean validateRefreshToken(String token, String expectedUsername) {
        String username = extractUsername(token);
        String tokenType = extractAllClaims(token).get("type", String.class);
        return username.equals(expectedUsername) && !isTokenExpired(token) && "REFRESH".equals(tokenType);
    }

    public boolean isTokenExpired(String token) {
        return extractAllClaims(token).getExpiration().before(new Date());
    }

    private Claims extractAllClaims(String token) {
        return Jwts.parser()
                .verifyWith((javax.crypto.SecretKey) getSignKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }
}