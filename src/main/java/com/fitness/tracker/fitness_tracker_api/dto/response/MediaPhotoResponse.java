package com.fitness.tracker.fitness_tracker_api.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDate;

@Schema(description = "Response DTO for media photo details")
public record MediaPhotoResponse(

        @Schema(example = "1", description = "Unique identifier of the media photo")
        Long id,

        @Schema(example = "progress_photo_01.jpg", description = "Original file name of the uploaded photo")
        String fileName,

        @Schema(example = "2025-11-04", description = "Date when photo uploaded")
        LocalDate createdAt) {
}
