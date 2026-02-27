package com.example.todos.dto;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.Builder;

@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public record ApiRespone<T>(
        String message,
        int status,
        T data,
        LocalDateTime timestamp) {

}
