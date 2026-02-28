package com.example.todos.dto;

import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.Builder;

@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public record TodoReq(
        String title,
        String description,
        Boolean completed) {

}
