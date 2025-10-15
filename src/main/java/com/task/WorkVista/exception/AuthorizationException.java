package com.task.WorkVista.exception;

import org.springframework.http.HttpStatus;

public class AuthorizationException extends AppException {
    public AuthorizationException(String message) {
        super(message, HttpStatus.FORBIDDEN.value());
    }
}