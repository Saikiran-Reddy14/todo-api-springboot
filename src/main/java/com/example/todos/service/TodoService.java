package com.example.todos.service;

import org.springframework.stereotype.Service;

import com.example.todos.dto.TodoReq;
import com.example.todos.dto.TodoRes;
import com.example.todos.entity.Todo;
import com.example.todos.entity.User;
import com.example.todos.exception.InvalidBody;
import com.example.todos.exception.ResourceNotFound;
import com.example.todos.repo.TodoRepo;
import com.example.todos.repo.UserRepo;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class TodoService {

    private final TodoRepo todoRepo;
    private final UserRepo userRepo;

    public TodoRes createTodo(TodoReq request, String username) {
        User user = userRepo.findByUsername(username)
            .orElseThrow(() -> new ResourceNotFound("User not found: " + username));
        if (request.title() == null || request.title().isBlank()) {
            throw new InvalidBody("Title is required");
        }
        if (request.description() == null || request.description().isBlank()) {
            throw new InvalidBody("Description is required");
        }
        Todo todo = todoRepo.save(Todo.builder().title(request.title()).description(request.description())
                .completed(false).user(user).build());
        return TodoRes.builder().id(todo.getId()).title(todo.getTitle()).description(todo.getDescription())
                .completed(todo.getCompleted()).build();
    }

}
