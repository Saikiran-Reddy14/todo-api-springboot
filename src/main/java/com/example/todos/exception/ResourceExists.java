package com.example.todos.exception;

public class ResourceExists extends RuntimeException {

    public ResourceExists(String message) {
        super(message);
    }

}
