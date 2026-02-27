package com.example.todos.dto;

import lombok.Builder;

@Builder
public record LoginRes(
        String accessToken,
        String refreshToken) {

}
