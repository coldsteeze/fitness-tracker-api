package com.fitness.tracker.fitness_tracker_api.exception.token;

import com.fitness.tracker.fitness_tracker_api.exception.AppException;
import com.fitness.tracker.fitness_tracker_api.exception.ErrorCode;

public class RefreshTokenNotFoundException extends AppException {
    public RefreshTokenNotFoundException(ErrorCode errorCode) {
        super(errorCode);
    }
}
