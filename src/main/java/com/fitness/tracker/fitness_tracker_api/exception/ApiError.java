package com.fitness.tracker.fitness_tracker_api.exception;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "API error response")
public class ApiError {

    @Schema(example = "400", description = "HTTP status code")
    private int status;

    @Schema(example = "Bad Request", description = "Short description of the error")
    private String error;

    @Schema(example = "username: Username is required, username: Username must be between 4 and 24 characters",
            description = "Detailed error message")
    private String message;

    @Schema(example = "/api/auth/register", description = "Path of the endpoint that caused the error")
    private String path;

    @Schema(example = "2025-11-01T00:00:00", description = "Timestamp when the error occurred")
    private LocalDateTime timestamp;
}

