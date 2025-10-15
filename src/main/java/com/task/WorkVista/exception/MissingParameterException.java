package com.task.WorkVista.exception;

import org.springframework.http.HttpStatus;

public class MissingParameterException extends AppException {
    public MissingParameterException(String message) {
        super(message, HttpStatus.BAD_REQUEST.value());
    }
}
