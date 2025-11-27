package com.fitness.tracker.fitness_tracker_api.exception.media;

import com.fitness.tracker.fitness_tracker_api.exception.AppException;
import org.springframework.http.HttpStatus;

public class MediaPhotoNotFoundException extends AppException {
    public MediaPhotoNotFoundException(String message) {
        super(message, HttpStatus.NOT_FOUND);
    }
}
