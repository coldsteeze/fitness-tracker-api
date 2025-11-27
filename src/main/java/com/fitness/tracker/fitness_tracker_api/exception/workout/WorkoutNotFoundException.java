package com.fitness.tracker.fitness_tracker_api.exception.workout;

import com.fitness.tracker.fitness_tracker_api.exception.AppException;
import com.fitness.tracker.fitness_tracker_api.exception.ErrorCode;

public class WorkoutNotFoundException extends AppException {
    public WorkoutNotFoundException(Long id) {
        super(ErrorCode.WORKOUT_NOT_FOUND.format(id), ErrorCode.WORKOUT_NOT_FOUND);
    }
}
