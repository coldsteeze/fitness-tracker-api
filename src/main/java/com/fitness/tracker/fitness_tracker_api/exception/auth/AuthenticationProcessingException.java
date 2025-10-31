package com.fitness.tracker.fitness_tracker_api.exception.auth;

public class AuthenticationProcessingException extends RuntimeException {
    public AuthenticationProcessingException(String message, Throwable cause) {
        super(message, cause);
    }
}
