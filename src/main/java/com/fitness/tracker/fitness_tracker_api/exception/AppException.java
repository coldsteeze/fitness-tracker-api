package com.fitness.tracker.fitness_tracker_api.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class AppException extends RuntimeException {

    private final HttpStatus status;

    protected AppException(ErrorCode errorCode) {
        super(errorCode.message);
        this.status = errorCode.status;
    }

    protected AppException(ErrorCode errorCode, Throwable cause) {
        super(errorCode.message, cause);
        this.status = errorCode.status;
    }

    protected AppException(String message, ErrorCode errorCode) {
        super(message);
        this.status = errorCode.status;
    }
}
