package com.fitness.tracker.fitness_tracker_api.exception.media;

import com.fitness.tracker.fitness_tracker_api.exception.AppException;
import org.springframework.http.HttpStatus;

public class MediaPhotoAccessDeniedException extends AppException {
    public MediaPhotoAccessDeniedException(String message) {
        super(message, HttpStatus.FORBIDDEN);
    }
}
