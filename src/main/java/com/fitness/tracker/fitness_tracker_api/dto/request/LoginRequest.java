package com.fitness.tracker.fitness_tracker_api.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Schema(description = "User login request")
public class LoginRequest {

    @NotBlank(message = "Identifier is required")
    @Size(max = 100, message = "Identifier too long")
    @Schema(example = "nikita_korobkin14", description = "Username or email for login")
    private String identifier;

    @NotBlank(message = "Password is required")
    @Schema(example = "Password123%", description = "User password")
    private String password;
}
