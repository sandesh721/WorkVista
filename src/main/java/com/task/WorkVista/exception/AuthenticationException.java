package com.task.WorkVista.exception;

import org.springframework.http.HttpStatus;

public class AuthenticationException extends AppException {
    public AuthenticationException(String message) {
        super(message, HttpStatus.UNAUTHORIZED.value());
    }
}