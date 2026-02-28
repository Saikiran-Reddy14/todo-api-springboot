package com.example.todos.dto;

import java.util.List;

import lombok.Builder;

@Builder
public record TodoPagination(
        List<TodoRes> todos,
        Integer pageNumber,
        Integer pageSize,
        Integer totalPages,
        Long totalElements) {

}
