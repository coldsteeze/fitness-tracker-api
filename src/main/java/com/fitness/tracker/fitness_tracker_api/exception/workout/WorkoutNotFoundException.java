package com.fitness.tracker.fitness_tracker_api.exception.workout;

public class WorkoutNotFoundException extends RuntimeException {
    public WorkoutNotFoundException(String message) {
        super(message);
    }
}
