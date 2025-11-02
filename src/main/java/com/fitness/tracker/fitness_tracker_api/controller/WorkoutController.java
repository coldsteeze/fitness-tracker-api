package com.fitness.tracker.fitness_tracker_api.controller;

import com.fitness.tracker.fitness_tracker_api.dto.request.WorkoutRequest;
import com.fitness.tracker.fitness_tracker_api.dto.response.WorkoutResponse;
import com.fitness.tracker.fitness_tracker_api.security.user.UserDetailsImpl;
import com.fitness.tracker.fitness_tracker_api.service.WorkoutService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/workouts")
public class WorkoutController {

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
}
