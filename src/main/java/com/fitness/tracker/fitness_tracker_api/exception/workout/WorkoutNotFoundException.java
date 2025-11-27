package com.fitness.tracker.fitness_tracker_api.exception.workout;

import com.fitness.tracker.fitness_tracker_api.exception.AppException;
import org.springframework.http.HttpStatus;

public class WorkoutNotFoundException extends AppException {
    public WorkoutNotFoundException(Long id) {
        super("Workout with id " + id + " not found", HttpStatus.NOT_FOUND);
    }
}
