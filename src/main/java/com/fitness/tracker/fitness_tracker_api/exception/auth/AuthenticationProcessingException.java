package com.fitness.tracker.fitness_tracker_api.exception.auth;

import com.fitness.tracker.fitness_tracker_api.exception.AppException;
import com.fitness.tracker.fitness_tracker_api.exception.ErrorCode;

public class AuthenticationProcessingException extends AppException {
    public AuthenticationProcessingException(ErrorCode errorCode, Throwable cause) {
        super(errorCode, cause);
    }
}
