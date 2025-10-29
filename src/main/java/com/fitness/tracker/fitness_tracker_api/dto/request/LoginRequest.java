package com.fitness.tracker.fitness_tracker_api.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LoginRequest {

    @NotBlank(message = "Identifier is required")
    @Size(max = 100, message = "Identifier too long")
    private String identifier;

    @NotBlank(message = "Password is required")
    private String password;
}
