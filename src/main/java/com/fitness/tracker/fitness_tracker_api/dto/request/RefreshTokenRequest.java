package com.fitness.tracker.fitness_tracker_api.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Schema(description = "Request to refresh access token")
public class RefreshTokenRequest {

    @NotBlank(message = "Refresh token is required")
    @Schema(example = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...", description = "Valid refresh token")
    private String refreshToken;
}
