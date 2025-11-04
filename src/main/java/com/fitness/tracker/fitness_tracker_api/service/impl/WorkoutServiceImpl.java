package com.fitness.tracker.fitness_tracker_api.service.impl;

import com.fitness.tracker.fitness_tracker_api.dto.request.WorkoutRequest;
import com.fitness.tracker.fitness_tracker_api.dto.response.PagedResponse;
import com.fitness.tracker.fitness_tracker_api.dto.response.WorkoutResponse;
import com.fitness.tracker.fitness_tracker_api.entity.User;
import com.fitness.tracker.fitness_tracker_api.entity.Workout;
import com.fitness.tracker.fitness_tracker_api.entity.enums.WorkoutType;
import com.fitness.tracker.fitness_tracker_api.exception.workout.WorkoutNotFoundException;
import com.fitness.tracker.fitness_tracker_api.mapper.WorkoutMapper;
import com.fitness.tracker.fitness_tracker_api.repository.WorkoutRepository;
import com.fitness.tracker.fitness_tracker_api.service.WorkoutService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class WorkoutServiceImpl implements WorkoutService {

    private final WorkoutRepository workoutRepository;
    private final WorkoutMapper workoutMapper;

    @Override
    @Transactional(readOnly = true)
    public WorkoutResponse findById(Long id, User user) {
        Workout workout = getWorkout(id, user);

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
        Workout workout = getWorkout(id, user);

        workoutMapper.updateEntityFromDto(workout, workoutRequest);
        workoutRepository.save(workout);
        log.info("Workout updated successfully: userId: {}, workoutId: {}", user.getId(), workout.getId());

        return workoutMapper.toDto(workout);
    }

    @Override
    @Transactional
    public void deleteWorkout(Long id, User user) {
        Workout workout = getWorkout(id, user);

        workoutRepository.delete(workout);
        log.info("Workout deleted successfully: userId: {}, workoutId: {}", user.getId(), workout.getId());
    }

    @Override
    @Transactional(readOnly = true)
    public PagedResponse<WorkoutResponse> findAllWorkouts(
            WorkoutType type,
            LocalDate dateStart,
            LocalDate dateEnd,
            Integer durationStart,
            Integer durationEnd,
            Pageable pageable,
            User user) {

        log.info("Fetching workouts: userId={}, type={}, start={}, end={}, " +
                        "durationStart={}, durationEnd={}, page={}, size={}, sort={}",
                user.getId(),
                type,
                dateStart,
                dateEnd,
                durationStart,
                durationEnd,
                pageable.getPageNumber(),
                pageable.getPageSize(),
                pageable.getSort());

        Page<Workout> page = workoutRepository.findAllByFilters(
                user,
                type,
                dateStart,
                dateEnd,
                durationStart,
                durationEnd,
                pageable
        );

        List<WorkoutResponse> content = page.stream()
                .map(workoutMapper::toDto)
                .toList();

        return new PagedResponse<>(
                content,
                page.getNumber(),
                page.getSize(),
                page.getTotalElements(),
                page.getTotalPages()
        );
    }

    private Workout getWorkout(Long id, User user) {
        return workoutRepository.findByIdAndUser(id, user)
                .orElseThrow(() -> new WorkoutNotFoundException(id));
    }
}
