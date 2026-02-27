package com.example.todos.dto;

import java.time.LocalDateTime;

import lombok.Builder;

@Builder
public record ApiRespone<T>(
        String message,
        int status,
        T data,
        LocalDateTime timestamp) {

}
