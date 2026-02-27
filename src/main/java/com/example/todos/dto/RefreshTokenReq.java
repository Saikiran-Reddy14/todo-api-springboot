package com.example.todos.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Builder;

@Builder
public record RefreshTokenReq(
        @NotBlank(message = "Refresh token is required") String refreshToken) {

}
