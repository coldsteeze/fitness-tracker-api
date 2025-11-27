package com.fitness.tracker.fitness_tracker_api.exception.token;

import com.fitness.tracker.fitness_tracker_api.exception.AppException;
import org.springframework.http.HttpStatus;

public class RefreshTokenNotFoundException extends AppException {
    public RefreshTokenNotFoundException(String message) {
        super(message, HttpStatus.UNAUTHORIZED);
    }
}
