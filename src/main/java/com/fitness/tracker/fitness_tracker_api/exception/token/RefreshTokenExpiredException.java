package com.fitness.tracker.fitness_tracker_api.exception.token;

import com.fitness.tracker.fitness_tracker_api.exception.AppException;
import com.fitness.tracker.fitness_tracker_api.exception.ErrorCode;

public class RefreshTokenExpiredException extends AppException {
    public RefreshTokenExpiredException(ErrorCode errorCode) {
        super(errorCode);
    }
}
