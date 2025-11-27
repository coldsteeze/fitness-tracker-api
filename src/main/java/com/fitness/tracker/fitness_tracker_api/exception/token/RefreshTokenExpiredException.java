package com.fitness.tracker.fitness_tracker_api.exception.token;

import com.fitness.tracker.fitness_tracker_api.exception.AppException;
import org.springframework.http.HttpStatus;

public class RefreshTokenExpiredException extends AppException {
    public RefreshTokenExpiredException(String message) {
        super(message, HttpStatus.UNAUTHORIZED);
    }
}
