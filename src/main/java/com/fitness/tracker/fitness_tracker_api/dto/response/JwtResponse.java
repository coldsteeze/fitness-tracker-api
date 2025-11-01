package com.fitness.tracker.fitness_tracker_api.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "JWT authentication response containing access and refresh tokens")
public record JwtResponse(

        @Schema(description = "Access token for authenticated requests",
                example = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...")
        String accessToken,

        @Schema(description = "Refresh token used to obtain a new access token",
                example = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...")
        String refreshToken
) {}
