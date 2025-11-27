package com.fitness.tracker.fitness_tracker_api.exception.auth;

import com.fitness.tracker.fitness_tracker_api.exception.AppException;
import org.springframework.http.HttpStatus;

public class AuthenticationProcessingException extends AppException {
    public AuthenticationProcessingException(String message, Throwable cause) {
        super(message, cause, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
