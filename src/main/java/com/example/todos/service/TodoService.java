package com.example.todos.service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.todos.dto.TodoPagination;
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

    @Transactional
    public TodoRes createTodo(TodoReq request, String username) {
        User user = getUserByUsername(username);
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

    private static final List<String> ALLOWED_SORT_FIELDS = List.of("id", "title", "completed");

    @Transactional(readOnly = true)
    public TodoPagination getTodos(String username, Integer pageNumber, Integer pageSize, String sortBy,
            String sortOrder) {
        if (pageNumber < 0) {
            throw new InvalidBody("Page number must be >= 0");
        }
        if (!ALLOWED_SORT_FIELDS.contains(sortBy)) {
            throw new InvalidBody("Invalid sort field. Allowed: " + ALLOWED_SORT_FIELDS);
        }
        User user = getUserByUsername(username);
        Sort sort = sortOrder.equalsIgnoreCase("asc") ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();
        Pageable pageable = PageRequest.of(pageNumber, pageSize, sort);
        Page<Todo> pageData = todoRepo.findByUserId(user.getId(), pageable);
        List<TodoRes> todos = pageData.getContent().stream()
                .map(todo -> TodoRes.builder().id(todo.getId()).title(todo.getTitle())
                        .description(todo.getDescription()).completed(todo.getCompleted()).build())
                .toList();
        return TodoPagination.builder().todos(todos).totalElements(pageData.getTotalElements())
                .totalPages(pageData.getTotalPages()).pageNumber(pageData.getNumber()).pageSize(pageData.getSize())
                .hasNext(pageData.hasNext())
                .build();
    }

    @Transactional(readOnly = true)
    public TodoRes getTodoById(Long id, String username) {
        User user = getUserByUsername(username);
        Todo todo = todoRepo.findByIdAndUserId(id, user.getId())
                .orElseThrow(() -> new ResourceNotFound("Todo not found"));
        return TodoRes.builder().id(todo.getId()).title(todo.getTitle()).description(todo.getDescription())
                .completed(todo.getCompleted()).build();
    }

    @Transactional
    public TodoRes updateTodo(TodoReq request, Long id, String username) {
        User user = getUserByUsername(username);
        Todo todo = todoRepo.findByIdAndUserId(id, user.getId())
                .orElseThrow(() -> new ResourceNotFound("Todo not found"));

        if (request.title() != null && !request.title().isBlank()) {
            todo.setTitle(request.title());
        }
        if (request.description() != null && !request.description().isBlank()) {
            todo.setDescription(request.description());
        }
        if (request.completed() != null) {
            todo.setCompleted(request.completed());
        }
        Todo updatedTodo = todoRepo.save(todo);
        return TodoRes.builder().id(updatedTodo.getId()).title(updatedTodo.getTitle())
                .description(updatedTodo.getDescription()).completed(updatedTodo.getCompleted()).build();
    }

    @Transactional
    public void deleteTodo(Long id, String username) {
        User user = getUserByUsername(username);
        Todo todo = todoRepo.findByIdAndUserId(id, user.getId())
                .orElseThrow(() -> new ResourceNotFound("Todo not found"));
        todoRepo.delete(todo);
    }

    public User getUserByUsername(String username) {
        return userRepo.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFound("User not found: " + username));
    }

}
