package com.fitness.tracker.fitness_tracker_api.dto.response;

import com.fitness.tracker.fitness_tracker_api.entity.enums.WorkoutType;

import java.time.LocalDate;

public record WorkoutResponse(
        Long id,
        String name,
        WorkoutType type,
        LocalDate date,
        Integer duration,
        Integer calories,
        String notes
) {
}
