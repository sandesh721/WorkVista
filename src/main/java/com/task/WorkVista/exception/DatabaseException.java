package com.task.WorkVista.exception;

import org.springframework.http.HttpStatus;

public class DatabaseException extends AppException {
    public DatabaseException(String message) {
        super(message, HttpStatus.INTERNAL_SERVER_ERROR.value());
    }
}