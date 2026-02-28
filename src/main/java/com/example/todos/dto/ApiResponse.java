package com.example.todos.dto;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.Builder;

@Builder
public record ApiResponse<T>(
                String message,
                int status,
                @JsonInclude(JsonInclude.Include.NON_NULL) T data,
                LocalDateTime timestamp) {

}
