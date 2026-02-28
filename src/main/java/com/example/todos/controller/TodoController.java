package com.example.todos.controller;

import java.time.LocalDateTime;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.todos.dto.ApiResponse;
import com.example.todos.dto.TodoPagination;
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

    @GetMapping("")
    public ResponseEntity<ApiResponse<TodoPagination>> getTodos(
            @RequestParam(defaultValue = "0") Integer pageNumber,
            @RequestParam(defaultValue = "5") Integer pageSize,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "desc") String sortOrder,
            @AuthenticationPrincipal CustomUserDetails userDetails) {
        TodoPagination response = todoService.getTodos(userDetails.getUsername(), pageNumber, pageSize, sortBy,
                sortOrder);
        return ResponseEntity
                .ok(ApiResponse.<TodoPagination>builder().message("Todos retrieved successfully").status(200)
                        .data(response).timestamp(LocalDateTime.now()).build());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<TodoRes>> getTodoById(@PathVariable Long id,
            @AuthenticationPrincipal CustomUserDetails userDetails) {
        TodoRes response = todoService.getTodoById(id, userDetails.getUsername());
        return ResponseEntity.ok(ApiResponse.<TodoRes>builder().message("Todo retrieved successfully").status(200)
                .data(response).timestamp(LocalDateTime.now()).build());
    }

    @PatchMapping("/{id}")
    public ResponseEntity<ApiResponse<TodoRes>> updateTodo(@RequestBody TodoReq request, @PathVariable Long id,
            @AuthenticationPrincipal CustomUserDetails userDetails) {
        TodoRes response = todoService.updateTodo(request, id, userDetails.getUsername());
        return ResponseEntity.ok()
                .body(ApiResponse.<TodoRes>builder().message("Todo updated successfully")
                        .status(200).data(response).timestamp(LocalDateTime.now()).build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteTodo(@PathVariable Long id,
            @AuthenticationPrincipal CustomUserDetails userDetails) {
        todoService.deleteTodo(id, userDetails.getUsername());
        return ResponseEntity.ok()
                .body(ApiResponse.<Void>builder().message("Todo deleted successfully").status(200)
                        .timestamp(LocalDateTime.now()).build());
    }

}
