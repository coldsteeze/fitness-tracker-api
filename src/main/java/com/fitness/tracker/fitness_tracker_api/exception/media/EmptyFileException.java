package com.fitness.tracker.fitness_tracker_api.exception.media;

import com.fitness.tracker.fitness_tracker_api.exception.AppException;
import org.springframework.http.HttpStatus;

public class EmptyFileException extends AppException {
    public EmptyFileException(String message) {
        super(message, HttpStatus.BAD_REQUEST);
    }
}
