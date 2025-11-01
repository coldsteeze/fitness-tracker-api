package com.fitness.tracker.fitness_tracker_api.exception.token;

public class RefreshTokenNotFoundException extends RuntimeException {
    public RefreshTokenNotFoundException(String message) {
        super(message);
    }
}
