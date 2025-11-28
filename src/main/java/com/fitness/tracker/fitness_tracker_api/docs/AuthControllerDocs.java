package com.fitness.tracker.fitness_tracker_api.docs;

import com.fitness.tracker.fitness_tracker_api.dto.request.LoginRequest;
import com.fitness.tracker.fitness_tracker_api.dto.request.RefreshTokenRequest;
import com.fitness.tracker.fitness_tracker_api.dto.request.RegisterRequest;
import com.fitness.tracker.fitness_tracker_api.dto.response.JwtResponse;
import com.fitness.tracker.fitness_tracker_api.exception.ApiError;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

@Tag(name = "Auth", description = "Registration, login, token refresh, and logout")
public interface AuthControllerDocs {

    @Operation(
            summary = "Register a new user",
            description = "Creates a new user and returns JWT tokens",
            responses = {
                    @ApiResponse(
                            responseCode = "201",
                            description = "User successfully registered",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = JwtResponse.class)
                            )
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            description = "Validation error",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = ApiError.class),
                                    examples = @ExampleObject(value = """
                                            {
                                              "status": 400,
                                              "error": "Bad Request",
                                              "message": "username: Username is required, username: Username must be between 4 and 24 characters",
                                              "path": "/api/auth/register",
                                              "timestamp": "2025-11-01T00:00:00"
                                            }
                                            """)
                            )
                    ),
                    @ApiResponse(
                            responseCode = "409",
                            description = "User already exists",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = ApiError.class),
                                    examples = {
                                            @ExampleObject(value = """
                                                    {
                                                      "status": 409,
                                                      "error": "Conflict",
                                                      "message": "Email already exists",
                                                      "path": "/api/auth/register",
                                                      "timestamp": "2025-11-01T00:00:00"
                                                    }
                                                    """),
                                            @ExampleObject(value = """
                                            {
                                              "status": 409,
                                              "error": "Conflict",
                                              "message": "Username already exists",
                                              "path": "/api/auth/register",
                                              "timestamp": "2025-11-01T00:00:00"
                                            }
                                            """)
                                    }
                            )
                    )
            }
    )
    ResponseEntity<JwtResponse> register(@RequestBody(
            description = "Registration data",
            required = true,
            content = @Content(
                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                    schema = @Schema(implementation = RegisterRequest.class)
            )
    ) RegisterRequest registerRequest);

    @Operation(
            summary = "User login",
            description = "Returns JWT tokens upon successful authentication",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Login successful",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = JwtResponse.class)
                            )
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            description = "Validation error",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = ApiError.class),
                                    examples = @ExampleObject(value = """
                                            {
                                              "status": 400,
                                              "error": "Bad Request",
                                              "message": "identifier: Identifier is required",
                                              "path": "/api/auth/login",
                                              "timestamp": "2025-11-01T00:00:00"
                                            }
                                            """)
                            )
                    ),
                    @ApiResponse(
                            responseCode = "401",
                            description = "Invalid login credentials",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = ApiError.class),
                                    examples = @ExampleObject(value = """
                                            {
                                              "status": 401,
                                              "error": "Unauthorized",
                                              "message": "Invalid identifier or password",
                                              "path": "/api/auth/login",
                                              "timestamp": "2025-11-01T00:00:00"
                                            }
                                            """)
                            )
                    ),
                    @ApiResponse(
                            responseCode = "500",
                            description = "Internal server error",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = ApiError.class),
                                    examples = @ExampleObject(value = """
                                            {
                                              "status": 500,
                                              "error": "Internal Server Error",
                                              "message": "Unexpected server error",
                                              "path": "/api/auth/login",
                                              "timestamp": "2025-11-01T00:00:00"
                                            }
                                            """)
                            )
                    )
            }
    )
    ResponseEntity<JwtResponse> login(
            @RequestBody(
                    description = "Login data",
                    required = true,
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = LoginRequest.class)
                    )
            ) LoginRequest loginRequest
    );

    @Operation(
            summary = "Refresh JWT tokens",
            description = "Returns new access and refresh tokens",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Tokens refreshed",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = JwtResponse.class)
                            )
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            description = "Validation error",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = ApiError.class),
                                    examples = @ExampleObject(value = """
                                            {
                                              "status": 400,
                                              "error": "Bad Request",
                                              "message": "refreshToken: Refresh token is required",
                                              "path": "/api/auth/refresh",
                                              "timestamp": "2025-11-01T00:00:00"
                                            }
                                            """)
                            )
                    ),
                    @ApiResponse(
                            responseCode = "401",
                            description = "Invalid or expired refresh token",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = ApiError.class),
                                    examples = @ExampleObject(value = """
                                            {
                                              "status": 401,
                                              "error": "Unauthorized",
                                              "message": "Refresh token expired",
                                              "path": "/api/auth/refresh",
                                              "timestamp": "2025-11-01T00:00:00"
                                            }
                                            """)
                            )
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Refresh token not found",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = ApiError.class),
                                    examples = @ExampleObject(value = """
                                            {
                                              "status": 404,
                                              "error": "Not Found",
                                              "message": "Refresh token not found",
                                              "path": "/api/auth/refresh",
                                              "timestamp": "2025-11-01T00:00:00"
                                            }
                                            """)
                            )
                    )
            }
    )
    ResponseEntity<JwtResponse> refresh(
            @RequestBody(
                    description = "Refresh token data",
                    required = true,
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = RefreshTokenRequest.class)
                    )
            ) RefreshTokenRequest tokenRequest
    );

    @Operation(
            summary = "Logout user",
            description = "Deletes the refresh token and ends the session",
            responses = {
                    @ApiResponse(
                            responseCode = "204",
                            description = "Logout successful"
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            description = "Validation error",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = ApiError.class),
                                    examples = @ExampleObject(value = """
                                            {
                                              "status": 400,
                                              "error": "Bad Request",
                                              "message": "refreshToken: Refresh token is required",
                                              "path": "/api/auth/logout",
                                              "timestamp": "2025-11-01T00:00:00"
                                            }
                                            """)
                            )
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Refresh token not found",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = ApiError.class),
                                    examples = @ExampleObject(value = """
                                            {
                                              "status": 404,
                                              "error": "Not Found",
                                              "message": "Refresh token not found",
                                              "path": "/api/auth/logout",
                                              "timestamp": "2025-11-01T00:00:00"
                                            }
                                            """)
                            )
                    )
            }
    )
    ResponseEntity<Void> logout(
            @RequestBody(
                    description = "Refresh token for logout",
                    required = true,
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = RefreshTokenRequest.class)
                    )
            )
            RefreshTokenRequest tokenRequest
    );
}
