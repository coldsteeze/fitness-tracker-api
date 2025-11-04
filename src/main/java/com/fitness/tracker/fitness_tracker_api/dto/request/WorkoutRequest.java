package com.fitness.tracker.fitness_tracker_api.dto.request;

import com.fitness.tracker.fitness_tracker_api.entity.enums.WorkoutType;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@Schema(description = "Request DTO for creating or updating a workout")
public class WorkoutRequest {

    @NotBlank(message = "Name is required")
    @Size(max = 100, message = "Name too long")
    @Schema(example = "Morning Yoga", description = "Name of the workout")
    private String name;

    @NotNull(message = "Workout type is required")
    @Schema(example = "YOGA", description = "Type of workout (e.g., CARDIO, STRENGTH, YOGA)")
    private WorkoutType type;

    @NotNull(message = "Date is required")
    @PastOrPresent(message = "Date must not be in the future")
    @Schema(example = "2025-11-01", description = "Date of the workout (cannot be in the future)")
    private LocalDate date;

    @NotNull(message = "Duration is required")
    @Positive(message = "Duration must be positive")
    @Schema(example = "75", description = "Duration of the workout in minutes")
    private Integer duration;

    @NotNull(message = "Calories is required")
    @Positive(message = "Calories must be positive")
    @Schema(example = "600", description = "Calories burned during the workout")
    private Integer calories;

    @Size(max = 500, message = "Notes too long")
    @Schema(example = "My first note", description = "Optional notes about the workout")
    private String notes;
}
