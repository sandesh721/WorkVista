package com.task.WorkVista.exception;

import org.springframework.http.HttpStatus;

public class TransactionException extends AppException {
    public TransactionException(String message) {
        super(message, HttpStatus.CONFLICT.value());
    }
}