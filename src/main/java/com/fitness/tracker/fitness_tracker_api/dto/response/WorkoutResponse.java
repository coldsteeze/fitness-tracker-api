package com.fitness.tracker.fitness_tracker_api.dto.response;

import com.fitness.tracker.fitness_tracker_api.entity.enums.WorkoutType;
import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDate;

@Schema(description = "Response DTO for workout details")
public record WorkoutResponse(

        @Schema(example = "1", description = "Unique identifier of the workout")
        Long id,

        @Schema(example = "Morning Yoga", description = "Name of the workout")
        String name,

        @Schema(example = "YOGA", description = "Type of workout (e.g., CARDIO, STRENGTH, YOGA)")
        WorkoutType type,

        @Schema(example = "2025-11-01", description = "Date of the workout")
        LocalDate date,

        @Schema(example = "75", description = "Duration of the workout in minutes")
        Integer duration,

        @Schema(example = "600", description = "Calories burned during the workout")
        Integer calories,

        @Schema(example = "My first note", description = "Optional notes about the workout")
        String notes
) {
}
