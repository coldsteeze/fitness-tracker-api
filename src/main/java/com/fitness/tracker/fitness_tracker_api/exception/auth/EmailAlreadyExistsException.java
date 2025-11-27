package com.fitness.tracker.fitness_tracker_api.exception.auth;

import com.fitness.tracker.fitness_tracker_api.exception.AppException;
import com.fitness.tracker.fitness_tracker_api.exception.ErrorCode;

public class EmailAlreadyExistsException extends AppException {
    public EmailAlreadyExistsException(ErrorCode errorCode) {
        super(errorCode);
    }
}
