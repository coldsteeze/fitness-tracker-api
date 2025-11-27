package com.fitness.tracker.fitness_tracker_api.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class AppException extends RuntimeException {

    private final HttpStatus status;

    protected AppException(String message, HttpStatus status) {
        super(message);
        this.status = status;
    }

    protected AppException(String message, Throwable cause, HttpStatus status) {
        super(message, cause);
        this.status = status;
    }
}
