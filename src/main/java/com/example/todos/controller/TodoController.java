package com.example.todos.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.todos.dto.ApiResponse;
import com.example.todos.dto.TodoReq;
import com.example.todos.dto.TodoRes;
import com.example.todos.service.TodoService;
import com.example.todos.utils.CustomUserDetails;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/todos")
@RequiredArgsConstructor
public class TodoController {

    private final TodoService todoService;

    @PostMapping("")
    public ResponseEntity<ApiResponse<TodoRes>> createTodo(@RequestBody TodoReq request,
            @AuthenticationPrincipal CustomUserDetails userDetails) {
        TodoRes response = todoService.createTodo(request, userDetails.getUsername());
        return ResponseEntity.status(201)
                .body(ApiResponse.<TodoRes>builder().message("Todo created successfully").status(201)
                        .data(response).build());
    }

}
