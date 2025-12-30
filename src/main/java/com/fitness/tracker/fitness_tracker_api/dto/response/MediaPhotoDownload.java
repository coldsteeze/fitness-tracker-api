package com.fitness.tracker.fitness_tracker_api.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;

import java.io.InputStream;

@Schema(description = "Data required to stream a media file to the client")
public record MediaPhotoDownload(

        @Schema(
                description = "Binary input stream of the media file"
        )
        InputStream inputStream,

        @Schema(
                description = "MIME type of the media file",
                example = "image/jpeg"
        )
        String contentType,

        @Schema(
                description = "File size in bytes",
                example = "245678"
        )
        Long size
) {
}

