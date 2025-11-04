package com.fitness.tracker.fitness_tracker_api.dto.response;

import java.time.LocalDate;

public record MediaPhotoResponse(Long id, String fileName, LocalDate createdAt) {
}
