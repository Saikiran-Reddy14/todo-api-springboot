package com.example.todos.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Builder;

@Builder
public record LoginReq(
        @NotBlank(message = "Username is required") String username,
        @NotBlank(message = "Password is required") String password) {

}
