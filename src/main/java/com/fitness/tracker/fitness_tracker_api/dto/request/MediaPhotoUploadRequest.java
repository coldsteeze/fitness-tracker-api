package com.fitness.tracker.fitness_tracker_api.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

@Getter
@Setter
@Schema(description = "Photo upload request")
public class MediaPhotoUploadRequest {

    @Schema(description = "ID of the workout", example = "1")
    private Long workoutId;

    @Schema(description = "Photo file to upload", type = "string", format = "binary")
    private MultipartFile file;
}
