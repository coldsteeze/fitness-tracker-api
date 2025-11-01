package com.fitness.tracker.fitness_tracker_api.exception.token;

public class RefreshTokenExpiredException extends RuntimeException {
    public RefreshTokenExpiredException(String message) {
        super(message);
    }
}
