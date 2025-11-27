package com.fitness.tracker.fitness_tracker_api.exception.auth;

import com.fitness.tracker.fitness_tracker_api.exception.AppException;
import org.springframework.http.HttpStatus;

public class UsernameAlreadyExistsException extends AppException {
    public UsernameAlreadyExistsException(String message) {
        super(message, HttpStatus.CONFLICT);
    }
}
