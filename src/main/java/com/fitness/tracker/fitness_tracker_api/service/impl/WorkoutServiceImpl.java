package com.fitness.tracker.fitness_tracker_api.service.impl;

import com.fitness.tracker.fitness_tracker_api.dto.request.WorkoutRequest;
import com.fitness.tracker.fitness_tracker_api.dto.response.WorkoutResponse;
import com.fitness.tracker.fitness_tracker_api.entity.User;
import com.fitness.tracker.fitness_tracker_api.entity.Workout;
import com.fitness.tracker.fitness_tracker_api.mapper.WorkoutMapper;
import com.fitness.tracker.fitness_tracker_api.repository.WorkoutRepository;
import com.fitness.tracker.fitness_tracker_api.service.WorkoutService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
public class WorkoutServiceImpl implements WorkoutService {

    private final WorkoutRepository workoutRepository;
    private final WorkoutMapper workoutMapper;

    @Override
    @Transactional(readOnly = true)
    public WorkoutResponse findById(Long id, User user) {
        Workout workout = workoutRepository.findByIdAndUser(id, user)
                .orElseThrow(() -> new RuntimeException("Workout with this id not found"));

        return workoutMapper.toDto(workout);
    }

    @Override
    @Transactional
    public WorkoutResponse createWorkout(WorkoutRequest workoutRequest, User user) {
        Workout workout = workoutMapper.toEntity(workoutRequest);
        workout.setUser(user);
        workoutRepository.save(workout);
        log.info("Workout created successfully: userId: {}, workoutId: {}", user.getId(), workout.getId());

        return workoutMapper.toDto(workout);
    }

    @Override
    @Transactional
    public WorkoutResponse updateWorkout(Long id, WorkoutRequest workoutRequest, User user) {
        Workout workout = workoutRepository.findByIdAndUser(id, user)
                .orElseThrow(() -> new RuntimeException("Workout with this id not found"));

        workoutMapper.updateEntityFromDto(workout, workoutRequest);
        workoutRepository.save(workout);
        log.info("Workout updated successfully: userId: {}, workoutId: {}", user.getId(), workout.getId());

        return workoutMapper.toDto(workout);
    }

    @Override
    @Transactional
    public void deleteWorkout(Long id, User user) {
        Workout workout = workoutRepository.findByIdAndUser(id, user)
                .orElseThrow(() -> new RuntimeException("Workout with this id not found"));

        workoutRepository.delete(workout);
        log.info("Workout deleted successfully: userId: {}, workoutId: {}", user.getId(), workout.getId());
    }
}
