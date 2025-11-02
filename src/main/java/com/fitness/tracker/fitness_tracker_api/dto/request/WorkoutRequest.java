package com.fitness.tracker.fitness_tracker_api.dto.request;

import com.fitness.tracker.fitness_tracker_api.entity.enums.WorkoutType;
import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class WorkoutRequest {

    @NotBlank(message = "Name is required")
    @Size(max = 100, message = "Name too long")
    private String name;

    @NotNull(message = "Workout type is required")
    private WorkoutType type;

    @NotNull(message = "Date is required")
    @PastOrPresent(message = "Date must not be in the future")
    private LocalDate date;

    @NotNull(message = "Duration is required")
    @Positive(message = "Duration must be positive")
    private Integer duration;

    @NotNull(message = "Calories is required")
    @Positive(message = "Calories must be positive")
    private Integer calories;

    @Size(max = 500, message = "Notes too long")
    private String notes;
}
