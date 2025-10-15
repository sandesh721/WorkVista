package com.task.WorkVista.exception;

import org.springframework.http.HttpStatus;

public class InvalidResourceStateException extends AppException {
    public InvalidResourceStateException(String message) {
        super(message, HttpStatus.BAD_REQUEST.value());
    }
}