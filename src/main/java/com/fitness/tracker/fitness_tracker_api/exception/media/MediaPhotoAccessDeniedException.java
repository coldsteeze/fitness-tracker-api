package com.fitness.tracker.fitness_tracker_api.exception.media;

import com.fitness.tracker.fitness_tracker_api.exception.AppException;
import com.fitness.tracker.fitness_tracker_api.exception.ErrorCode;

public class MediaPhotoAccessDeniedException extends AppException {
    public MediaPhotoAccessDeniedException(ErrorCode errorCode) {
        super(errorCode);
    }
}
