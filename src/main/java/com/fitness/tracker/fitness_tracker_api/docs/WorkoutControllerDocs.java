package com.fitness.tracker.fitness_tracker_api.docs;

import com.fitness.tracker.fitness_tracker_api.dto.request.WorkoutRequest;
import com.fitness.tracker.fitness_tracker_api.dto.response.PagedResponse;
import com.fitness.tracker.fitness_tracker_api.dto.response.WorkoutResponse;
import com.fitness.tracker.fitness_tracker_api.entity.enums.WorkoutType;
import com.fitness.tracker.fitness_tracker_api.exception.ApiError;
import com.fitness.tracker.fitness_tracker_api.security.user.UserDetailsImpl;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;

import java.time.LocalDate;

@Tag(name = "Workout", description = "Manage user workouts: create, update, delete, list, and filter workouts")
public interface WorkoutControllerDocs {

    @Operation(
            summary = "Get workout by ID",
            description = "Fetch detailed information about a specific workout belonging to the authenticated user",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Workout fetched successfully",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = WorkoutResponse.class)
                            )
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Workout not found",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = ApiError.class),
                                    examples = @ExampleObject(value = """
                                            {
                                              "status": 404,
                                              "error": "Not Found",
                                              "message": "Workout with id 1 not found",
                                              "path": "/api/workouts/1",
                                              "timestamp": "2025-11-03T00:00:00"
                                            }
                                            """)
                            )
                    ),
                    @ApiResponse(
                            responseCode = "401",
                            description = "Unauthorized (missing, invalid, or expired JWT)",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = ApiError.class),
                                    examples = {
                                            @ExampleObject(name = "Invalid or expired token", value = """
                                                    {
                                                      "status": 401,
                                                      "error": "Unauthorized",
                                                      "message": "Invalid or expired token",
                                                      "path": "/api/workouts/1",
                                                      "timestamp": "2025-11-03T00:00:00"
                                                    }
                                                    """)
                                    }
                            )
                    )
            }
    )
    ResponseEntity<WorkoutResponse> getWorkout(
            @Parameter(description = "Workout ID", example = "1") @PathVariable Long id,
            UserDetailsImpl userDetails
    );

    @Operation(
            summary = "Create new workout",
            description = "Create a new workout and return detailed information about it",
            responses = {
                    @ApiResponse(
                            responseCode = "201",
                            description = "Workout created successfully",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = WorkoutResponse.class)
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
                                              "message": "duration: Duration must be positive",
                                              "path": "/api/workouts",
                                              "timestamp": "2025-11-03T00:00:00"
                                            }
                                            """)
                            )
                    ),
                    @ApiResponse(
                            responseCode = "401",
                            description = "Unauthorized (missing, invalid, or expired JWT)",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = ApiError.class),
                                    examples = {
                                            @ExampleObject(name = "Invalid or expired token", value = """
                                                    {
                                                      "status": 401,
                                                      "error": "Unauthorized",
                                                      "message": "Invalid or expired token",
                                                      "path": "/api/workouts",
                                                      "timestamp": "2025-11-03T00:00:00"
                                                    }
                                                    """)
                                    }
                            )
                    )
            }
    )
    ResponseEntity<WorkoutResponse> createWorkout(
            @RequestBody(
                    description = "Workout creation data",
                    required = true,
                    content = @Content(
                            schema = @Schema(implementation = WorkoutRequest.class)
                    )
            ) WorkoutRequest workoutRequest, UserDetailsImpl userDetails
    );

    @Operation(
            summary = "Update workout",
            description = "Update full information about an existing workout",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Workout updated successfully",
                            content = @Content(schema = @Schema(implementation = WorkoutResponse.class))
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
                                              "message": "duration: Duration must be positive",
                                              "path": "/api/workouts/1",
                                              "timestamp": "2025-11-03T00:00:00"
                                            }
                                            """)
                            )
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Workout not found",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = ApiError.class),
                                    examples = @ExampleObject(value = """
                                            {
                                              "status": 404,
                                              "error": "Not Found",
                                              "message": "Workout with id 1 not found",
                                              "path": "/api/workouts/1",
                                              "timestamp": "2025-11-03T00:00:00"
                                            }
                                            """)
                            )
                    ),
                    @ApiResponse(
                            responseCode = "401",
                            description = "Unauthorized (missing, invalid, or expired JWT)",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = ApiError.class),
                                    examples = {
                                            @ExampleObject(name = "Invalid or expired token", value = """
                                                    {
                                                      "status": 401,
                                                      "error": "Unauthorized",
                                                      "message": "Invalid or expired token",
                                                      "path": "/api/workouts/1",
                                                      "timestamp": "2025-11-03T00:00:00"
                                                    }
                                                    """)
                                    }
                            )
                    )
            }
    )
    ResponseEntity<WorkoutResponse> updateWorkout(
            @Parameter(description = "Workout ID", example = "1") @PathVariable Long id,
            @RequestBody(
                    description = "Workout update data",
                    required = true,
                    content = @Content(
                            schema = @Schema(implementation = WorkoutRequest.class)
                    )
            ) WorkoutRequest workoutRequest,
            UserDetailsImpl userDetails
    );

    @Operation(
            summary = "List workouts with filters",
            description = "Fetch paginated workouts with optional filters for type, date range, and duration range",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Workouts fetched successfully",
                            content = @Content(schema = @Schema(implementation = PagedResponse.class))
                    ),
                    @ApiResponse(
                            responseCode = "401",
                            description = "Unauthorized",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = ApiError.class),
                                    examples = {
                                            @ExampleObject(name = "Invalid or expired token", value = """
                                                    {
                                                      "status": 401,
                                                      "error": "Unauthorized",
                                                      "message": "Invalid or expired token",
                                                      "path": "/api/workouts",
                                                      "timestamp": "2025-11-03T00:00:00"
                                                    }
                                                    """)
                                    }
                            )
                    )
            }
    )
    ResponseEntity<PagedResponse<WorkoutResponse>> getWorkouts(
            @Parameter(description = "Workout type filter") WorkoutType type,
            @Parameter(description = "Start date (yyyy-MM-dd)") LocalDate dateStart,
            @Parameter(description = "End date (yyyy-MM-dd)") LocalDate dateEnd,
            @Parameter(description = "Minimum duration in minutes") Integer durationStart,
            @Parameter(description = "Maximum duration in minutes") Integer durationEnd,
            @ParameterObject Pageable pageable,
            UserDetailsImpl currentUser
    );

    @Operation(
            summary = "Delete workout",
            description = "Delete an existing workout by ID",
            responses = {
                    @ApiResponse(responseCode = "204", description = "Workout deleted successfully"),
                    @ApiResponse(
                            responseCode = "401",
                            description = "Unauthorized (missing, invalid, or expired JWT)",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = ApiError.class),
                                    examples = {
                                            @ExampleObject(name = "Invalid or expired token", value = """
                                                    {
                                                      "status": 401,
                                                      "error": "Unauthorized",
                                                      "message": "Invalid or expired token",
                                                      "path": "/api/workouts/1",
                                                      "timestamp": "2025-11-03T00:00:00"
                                                    }
                                                    """)
                                    }
                            )
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Workout not found",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = ApiError.class),
                                    examples = @ExampleObject(value = """
                                            {
                                              "status": 404,
                                              "error": "Not Found",
                                              "message": "Workout with id 1 not found",
                                              "path": "/api/workouts/1",
                                              "timestamp": "2025-11-03T00:00:00"
                                            }
                                            """)
                            )
                    ),
            }
    )
    ResponseEntity<Void> deleteWorkout(
            @Parameter(description = "Workout ID", example = "1") @PathVariable Long id,
            UserDetailsImpl userDetails
    );
}


