package com.example.todos.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.todos.entity.BlacklistedToken;

@Repository
public interface BlacklistedTokenRepo extends JpaRepository<BlacklistedToken, Long> {

    boolean existsByToken(String token);
}
