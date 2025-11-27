package com.fitness.tracker.fitness_tracker_api.exception.auth;

import com.fitness.tracker.fitness_tracker_api.exception.AppException;
import com.fitness.tracker.fitness_tracker_api.exception.ErrorCode;

public class InvalidCredentialsException extends AppException {
    public InvalidCredentialsException(ErrorCode errorCode) {
        super(errorCode);
    }
}
