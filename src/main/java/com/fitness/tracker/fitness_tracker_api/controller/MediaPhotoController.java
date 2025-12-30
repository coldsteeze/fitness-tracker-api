package com.fitness.tracker.fitness_tracker_api.controller;

import com.fitness.tracker.fitness_tracker_api.docs.MediaPhotoControllerDocs;
import com.fitness.tracker.fitness_tracker_api.dto.request.MediaPhotoUploadRequest;
import com.fitness.tracker.fitness_tracker_api.dto.response.MediaPhotoDownload;
import com.fitness.tracker.fitness_tracker_api.dto.response.MediaPhotoResponse;
import com.fitness.tracker.fitness_tracker_api.security.user.UserDetailsImpl;
import com.fitness.tracker.fitness_tracker_api.service.MediaPhotoService;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/media")
public class MediaPhotoController implements MediaPhotoControllerDocs {

    private final MediaPhotoService mediaPhotoService;

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<MediaPhotoResponse> createMediaPhoto(
            @ModelAttribute MediaPhotoUploadRequest request,
            @AuthenticationPrincipal UserDetailsImpl userDetails) {
        MediaPhotoResponse mediaResponse = mediaPhotoService.uploadPhoto(
                request.getWorkoutId(),
                userDetails.getUser(),
                request.getFile()
        );

        return ResponseEntity.status(HttpStatus.CREATED).body(mediaResponse);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Resource> getMediaPhoto(
            @PathVariable Long id,
            @AuthenticationPrincipal UserDetailsImpl userDetails) {
        MediaPhotoDownload photo = mediaPhotoService.getPhotoById(id, userDetails.getUser());

        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(photo.contentType()))
                .contentLength(photo.size())
                .body(new InputStreamResource(photo.inputStream()));
    }
}
