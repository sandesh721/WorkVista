package com.task.WorkVista.exception;

import org.springframework.http.HttpStatus;

public class InvalidFormatException extends AppException {
    public InvalidFormatException(String message) {
        super(message, HttpStatus.UNPROCESSABLE_ENTITY.value());
    }
}