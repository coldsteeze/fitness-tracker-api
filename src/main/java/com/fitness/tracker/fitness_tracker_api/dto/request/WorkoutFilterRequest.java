package com.fitness.tracker.fitness_tracker_api.dto.request;

import com.fitness.tracker.fitness_tracker_api.entity.enums.WorkoutType;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

@Getter
@Setter
@Schema(description = "Request filter DTO for search workouts")
public class WorkoutFilterRequest {

    @Schema(example = "YOGA", description = "Type of workout (e.g., CARDIO, STRENGTH, YOGA)")
    private WorkoutType type;

    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    @Schema(example = "2025-11-01", description = "Date of the start workout")
    private LocalDate dateStart;

    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    @Schema(example = "2025-11-02", description = "Date of the end workout")
    private LocalDate dateEnd;

    @Schema(example = "75", description = "Duration start of the workout in minutes")
    private Integer durationStart;

    @Schema(example = "90", description = "Duration end of the workout in minutes")
    private Integer durationEnd;
}
