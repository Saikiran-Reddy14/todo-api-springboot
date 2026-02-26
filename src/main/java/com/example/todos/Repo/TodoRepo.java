package com.example.todos.Repo;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.example.todos.entity.Todo;

public interface TodoRepo extends JpaRepository<Todo, Long> {

    Page<Todo> findByUserId(Long userId, Pageable pageable);

    Optional<Todo> findByIdAndUserId(Long id, Long userId);

}
