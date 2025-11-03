package com.fitness.tracker.fitness_tracker_api.controller;

import com.fitness.tracker.fitness_tracker_api.docs.WorkoutControllerDocs;
import com.fitness.tracker.fitness_tracker_api.dto.request.WorkoutRequest;
import com.fitness.tracker.fitness_tracker_api.dto.response.PagedResponse;
import com.fitness.tracker.fitness_tracker_api.dto.response.WorkoutResponse;
import com.fitness.tracker.fitness_tracker_api.entity.enums.WorkoutType;
import com.fitness.tracker.fitness_tracker_api.security.user.UserDetailsImpl;
import com.fitness.tracker.fitness_tracker_api.service.WorkoutService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/workouts")
public class WorkoutController implements WorkoutControllerDocs {

    private final WorkoutService workoutService;

    @GetMapping("/{id}")
    public ResponseEntity<WorkoutResponse> getWorkout(
            @PathVariable Long id,
            @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return ResponseEntity.ok(workoutService.findById(id, userDetails.getUser()));
    }

    @PostMapping
    public ResponseEntity<WorkoutResponse> createWorkout(
            @Valid @RequestBody WorkoutRequest workoutRequest,
            @AuthenticationPrincipal UserDetailsImpl userDetails) {
        WorkoutResponse workoutResponse = workoutService.createWorkout(workoutRequest, userDetails.getUser());
        return ResponseEntity.status(HttpStatus.CREATED).body(workoutResponse);
    }

    @PutMapping("/{id}")
    public ResponseEntity<WorkoutResponse> updateWorkout(
            @PathVariable Long id,
            @Valid @RequestBody WorkoutRequest workoutRequest,
            @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return ResponseEntity.ok(workoutService.updateWorkout(id, workoutRequest, userDetails.getUser()));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteWorkout(
            @PathVariable Long id,
            @AuthenticationPrincipal UserDetailsImpl userDetails) {
        workoutService.deleteWorkout(id, userDetails.getUser());
        return ResponseEntity.noContent().build();
    }

    @GetMapping
    public ResponseEntity<PagedResponse<WorkoutResponse>> getWorkouts(
            @RequestParam(required = false) WorkoutType type,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dateStart,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dateEnd,
            @RequestParam(required = false) Integer durationStart,
            @RequestParam(required = false) Integer durationEnd,
            Pageable pageable,
            @AuthenticationPrincipal UserDetailsImpl currentUser) {

        PagedResponse<WorkoutResponse> result = workoutService.findAllWorkouts(
                type,
                dateStart,
                dateEnd,
                durationStart,
                durationEnd,
                pageable,
                currentUser.getUser()
        );

        return ResponseEntity.ok(result);
    }
}
