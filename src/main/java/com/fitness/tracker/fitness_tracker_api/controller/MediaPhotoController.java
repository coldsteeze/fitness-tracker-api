package com.fitness.tracker.fitness_tracker_api.controller;

import com.fitness.tracker.fitness_tracker_api.dto.response.MediaPhotoResponse;
import com.fitness.tracker.fitness_tracker_api.entity.MediaPhoto;
import com.fitness.tracker.fitness_tracker_api.security.user.UserDetailsImpl;
import com.fitness.tracker.fitness_tracker_api.service.MediaPhotoService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.URLConnection;
import java.util.Optional;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/media")
public class MediaPhotoController {

    private final MediaPhotoService mediaPhotoService;

    @PostMapping
    public ResponseEntity<MediaPhotoResponse> createMediaPhoto(
            @RequestParam Long workoutId,
            @RequestParam MultipartFile file,
            @AuthenticationPrincipal UserDetailsImpl userDetails) throws IOException {
        MediaPhotoResponse mediaResponse = mediaPhotoService.uploadPhoto(
                workoutId,
                userDetails.getUser(),
                file.getOriginalFilename(),
                file.getBytes());

        return ResponseEntity.status(HttpStatus.CREATED).body(mediaResponse);
    }

    @GetMapping("/{id}")
    public ResponseEntity<byte[]> getMediaPhoto(
            @PathVariable Long id,
            @AuthenticationPrincipal UserDetailsImpl userDetails) {
        MediaPhoto photo = mediaPhotoService.getPhotoById(id, userDetails.getUser());

        String contentType = Optional.ofNullable(
                URLConnection.guessContentTypeFromName(photo.getFileName())
        ).orElse(MediaType.APPLICATION_OCTET_STREAM_VALUE);

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=\"" + photo.getFileName() + "\"")
                .contentType(MediaType.parseMediaType(contentType))
                .body(photo.getData());
    }
}
