package com.fitness.tracker.fitness_tracker_api.exception.workout;

public class WorkoutNotFoundException extends RuntimeException {
    public WorkoutNotFoundException(Long id) {
        super("Workout with id " + id + " not found");
    }
}
