package com.fitness.tracker.fitness_tracker_api.exception;

import com.auth0.jwt.exceptions.JWTVerificationException;
import com.fitness.tracker.fitness_tracker_api.exception.auth.AuthenticationProcessingException;
import com.fitness.tracker.fitness_tracker_api.exception.auth.EmailAlreadyExistsException;
import com.fitness.tracker.fitness_tracker_api.exception.auth.InvalidCredentialsException;
import com.fitness.tracker.fitness_tracker_api.exception.auth.UsernameAlreadyExistsException;
import com.fitness.tracker.fitness_tracker_api.exception.token.InvalidRefreshTokenException;
import com.fitness.tracker.fitness_tracker_api.exception.token.RefreshTokenExpiredException;
import com.fitness.tracker.fitness_tracker_api.exception.token.RefreshTokenNotFoundException;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;
import java.util.stream.Collectors;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    @ExceptionHandler(UsernameAlreadyExistsException.class)
    public ResponseEntity<ApiError> handleUsernameExists(UsernameAlreadyExistsException ex, HttpServletRequest request) {
        return buildErrorResponse(HttpStatus.CONFLICT, ex, request);
    }

    @ExceptionHandler(EmailAlreadyExistsException.class)
    public ResponseEntity<ApiError> handleEmailExists(EmailAlreadyExistsException ex, HttpServletRequest request) {
        return buildErrorResponse(HttpStatus.CONFLICT, ex, request);
    }

    @ExceptionHandler(InvalidCredentialsException.class)
    public ResponseEntity<ApiError> handleInvalidCredentials(InvalidCredentialsException ex, HttpServletRequest request) {
        return buildErrorResponse(HttpStatus.UNAUTHORIZED, ex, request);
    }

    @ExceptionHandler(AuthenticationProcessingException.class)
    public ResponseEntity<ApiError> handleAuthProcessing(Exception ex, HttpServletRequest request) {
        log.error("Unexpected error", ex);
        return buildErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR, ex, request);
    }

    @ExceptionHandler(RefreshTokenNotFoundException.class)
    public ResponseEntity<ApiError> handleRefreshTokenNotFound(RefreshTokenNotFoundException ex, HttpServletRequest request) {
        return buildErrorResponse(HttpStatus.NOT_FOUND, ex, request);
    }

    @ExceptionHandler(RefreshTokenExpiredException.class)
    public ResponseEntity<ApiError> handleRefreshTokenExpired(RefreshTokenExpiredException ex, HttpServletRequest request) {
        return buildErrorResponse(HttpStatus.UNAUTHORIZED, ex, request);
    }

    @ExceptionHandler(InvalidRefreshTokenException.class)
    public ResponseEntity<ApiError> handleInvalidRefresh(InvalidRefreshTokenException ex, HttpServletRequest request) {
        return buildErrorResponse(HttpStatus.UNAUTHORIZED, ex, request);
    }

    @ExceptionHandler(JWTVerificationException.class)
    public ResponseEntity<ApiError> handleJwtVerification(HttpServletRequest request) {
        return buildErrorResponse(HttpStatus.UNAUTHORIZED, new InvalidRefreshTokenException("Invalid or expired refresh token"), request);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiError> handleValidation(MethodArgumentNotValidException ex, HttpServletRequest request) {
        String message = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(err -> err.getField() + ": " + err.getDefaultMessage())
                .collect(Collectors.joining(", "));

        return buildErrorResponse(HttpStatus.BAD_REQUEST, new RuntimeException(message), request);
    }

    private ResponseEntity<ApiError> buildErrorResponse(HttpStatus status, Exception ex, HttpServletRequest request) {
        ApiError error = ApiError.builder()
                .status(status.value())
                .error(status.getReasonPhrase())
                .message(ex.getMessage())
                .path(request.getRequestURI())
                .timestamp(LocalDateTime.now())
                .build();

        return ResponseEntity.status(status).body(error);
    }
}
