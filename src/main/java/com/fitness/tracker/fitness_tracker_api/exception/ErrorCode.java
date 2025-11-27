package com.fitness.tracker.fitness_tracker_api.exception;

import org.springframework.http.HttpStatus;

public enum ErrorCode {
    USERNAME_EXISTS("Username already exists", HttpStatus.CONFLICT),
    EMAIL_EXISTS("Email already exists", HttpStatus.CONFLICT),
    INVALID_CREDENTIALS("Invalid identifier or password", HttpStatus.UNAUTHORIZED),
    EMPTY_FILE("File must not be empty", HttpStatus.BAD_REQUEST),
    MEDIA_PHOTO_ACCESS_DENIED("You do not have permission to access this photo", HttpStatus.FORBIDDEN),
    MEDIA_PHOTO_NOT_FOUND("Photo not found", HttpStatus.NOT_FOUND),
    TOKEN_INVALID("Invalid or expired token", HttpStatus.UNAUTHORIZED),
    TOKEN_EXPIRED("Refresh token expired", HttpStatus.UNAUTHORIZED),
    TOKEN_NOT_FOUND("Refresh token not found", HttpStatus.NOT_FOUND),
    WORKOUT_NOT_FOUND("Workout with id %d not found", HttpStatus.NOT_FOUND),
    VALIDATION_ERROR("Validation error", HttpStatus.BAD_REQUEST),
    INTERNAL_ERROR("Unexpected server error", HttpStatus.INTERNAL_SERVER_ERROR);

    public final String message;
    public final HttpStatus status;

    ErrorCode(String message, HttpStatus status) {
        this.message = message;
        this.status = status;
    }

    public String format(Object... args) {
        return String.format(this.message, args);
    }
}
