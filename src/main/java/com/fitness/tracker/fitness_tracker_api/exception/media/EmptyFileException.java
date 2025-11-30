package com.fitness.tracker.fitness_tracker_api.exception.media;

import com.fitness.tracker.fitness_tracker_api.exception.AppException;
import com.fitness.tracker.fitness_tracker_api.exception.ErrorCode;

public class EmptyFileException extends AppException {
    public EmptyFileException(ErrorCode errorCode) {
        super(errorCode);
    }
}
