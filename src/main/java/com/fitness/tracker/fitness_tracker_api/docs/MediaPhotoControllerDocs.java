package com.fitness.tracker.fitness_tracker_api.docs;

import com.fitness.tracker.fitness_tracker_api.dto.request.MediaPhotoUploadRequest;
import com.fitness.tracker.fitness_tracker_api.dto.response.MediaPhotoResponse;
import com.fitness.tracker.fitness_tracker_api.exception.ApiError;
import com.fitness.tracker.fitness_tracker_api.security.user.UserDetailsImpl;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;

import java.io.IOException;

@Tag(name = "Media", description = "Uploading and receiving photos")
public interface MediaPhotoControllerDocs {

    @Operation(
            summary = "Upload a media photo for a workout",
            description = "Uploads a photo to a specific workout. Requires authentication. The file must not be empty",
            responses = {
                    @ApiResponse(
                            responseCode = "201",
                            description = "Photo uploaded successfully",
                            content = @Content(schema = @Schema(implementation = MediaPhotoResponse.class))
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            description = "Bad request (e.g., empty file)",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = ApiError.class),
                                    examples = {
                                            @ExampleObject(name = "Empty file", value = """
                                                        {
                                                          "status": 400,
                                                          "error": "Bad Request",
                                                          "message": "File must not be empty",
                                                          "path": "/api/media",
                                                          "timestamp": "2025-11-04T12:06:58.4525898"
                                                        }
                                                    """)
                                    }
                            )
                    ),
                    @ApiResponse(
                            responseCode = "401",
                            description = "Unauthorized",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = ApiError.class),
                                    examples = {
                                            @ExampleObject(name = "Missing or invalid token", value = """
                                                    {
                                                      "status": 401,
                                                      "error": "Unauthorized",
                                                      "message": "Full authentication is required to access this resource",
                                                      "path": "/api/media",
                                                      "timestamp": "2025-11-04T00:00:00"
                                                    }
                                                    """),
                                            @ExampleObject(name = "Expired JWT", value = """
                                                    {
                                                      "status": 401,
                                                      "error": "Unauthorized",
                                                      "message": "Invalid or expired JWT",
                                                      "path": "/api/workouts",
                                                      "timestamp": "2025-11-04T00:00:00"
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
                                              "path": "/api/media",
                                              "timestamp": "2025-11-03T00:00:00"
                                            }
                                            """)
                            )
                    )
            }
    )
    ResponseEntity<MediaPhotoResponse> createMediaPhoto(
            @Parameter(description = "Media photo upload request containing workoutId and file")
            MediaPhotoUploadRequest request,
            UserDetailsImpl currentUser) throws IOException;


    @Operation(
            summary = "Get a media photo by ID",
            description = "Fetches a media photo by its ID. Requires authentication. Only the owner of the workout can access the photo.",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Media photo fetched successfully",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_OCTET_STREAM_VALUE,
                                    schema = @Schema(type = "string", format = "binary")
                            )
                    ),
                    @ApiResponse(
                            responseCode = "401",
                            description = "Unauthorized",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = ApiError.class),
                                    examples = {
                                            @ExampleObject(name = "Missing or invalid token", value = """
                                                    {
                                                      "status": 401,
                                                      "error": "Unauthorized",
                                                      "message": "Full authentication is required to access this resource",
                                                      "path": "/api/media/1",
                                                      "timestamp": "2025-11-04T00:00:00"
                                                    }
                                                    """),
                                            @ExampleObject(name = "Expired JWT", value = """
                                                    {
                                                      "status": 401,
                                                      "error": "Unauthorized",
                                                      "message": "Invalid or expired JWT",
                                                      "path": "/api/media/1",
                                                      "timestamp": "2025-11-04T00:00:00"
                                                    }
                                                    """)
                                    }
                            )
                    ),
                    @ApiResponse(
                            responseCode = "403",
                            description = "Forbidden (user tries to access someone else's photo)",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = ApiError.class),
                                    examples = @ExampleObject(value = """
                                            {
                                              "status": 403,
                                              "error": "Forbidden",
                                              "message": "You do not have permission to view this photo",
                                              "path": "/api/media/1",
                                              "timestamp": "2025-11-04T00:00:00"
                                            }
                                            """)
                            )
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Media photo not found",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = ApiError.class),
                                    examples = @ExampleObject(value = """
                                            {
                                              "status": 404,
                                              "error": "Not Found",
                                              "message": "Photo with id 1 not found",
                                              "path": "/api/media/1",
                                              "timestamp": "2025-11-04T00:00:00"
                                            }
                                            """)
                            )
                    )
            }
    )
    ResponseEntity<byte[]> getMediaPhoto(
            @Parameter(description = "Media photo ID", example = "1") @PathVariable Long id,
            UserDetailsImpl currentUser
    );
}
