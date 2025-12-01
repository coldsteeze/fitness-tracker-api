package com.fitness.tracker.fitness_tracker_api.service;

import com.fitness.tracker.fitness_tracker_api.dto.request.WorkoutFilterRequest;
import com.fitness.tracker.fitness_tracker_api.dto.request.WorkoutRequest;
import com.fitness.tracker.fitness_tracker_api.dto.response.PagedResponse;
import com.fitness.tracker.fitness_tracker_api.dto.response.WorkoutResponse;
import com.fitness.tracker.fitness_tracker_api.entity.User;
import org.springframework.data.domain.Pageable;

public interface WorkoutService {

    WorkoutResponse findById(Long id, User user);

    WorkoutResponse createWorkout(WorkoutRequest workoutRequest, User user);

    WorkoutResponse updateWorkout(Long id, WorkoutRequest workoutRequest, User user);

    void deleteWorkout(Long id, User user);

    PagedResponse<WorkoutResponse> findAllWorkouts(
            WorkoutFilterRequest workoutFilterRequest,
            Pageable pageable,
            User user);
}
