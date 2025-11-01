package com.fitness.tracker.fitness_tracker_api.dto.request;


import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Schema(description = "User registration request")
public class RegisterRequest {

    @NotBlank(message = "Username is required")
    @Size(min = 4, max = 24, message = "Username must be between 4 and 24 characters")
    @Schema(example = "nikita_korobkin14", description = "Unique username")
    private String username;

    @NotBlank(message = "Email is required")
    @Email(message = "Invalid email format")
    @Schema(example = "nikitakorobkin@mail.com", description = "Valid email address")
    private String email;

    @NotBlank(message = "Password is required")
    @Size(min = 8, max = 72, message = "Password must be between 8 and 72 characters")
    @Schema(example = "Password123%", description = "User password (minimum 8 characters)")
    private String password;
}
