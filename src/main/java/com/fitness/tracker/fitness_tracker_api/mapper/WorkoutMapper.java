package com.fitness.tracker.fitness_tracker_api.mapper;

import com.fitness.tracker.fitness_tracker_api.dto.request.WorkoutRequest;
import com.fitness.tracker.fitness_tracker_api.dto.response.WorkoutResponse;
import com.fitness.tracker.fitness_tracker_api.entity.Workout;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface WorkoutMapper {

    Workout toEntity(WorkoutRequest workoutRequest);

    WorkoutResponse toDto(Workout workout);

    void updateEntityFromDto(@MappingTarget Workout workout, WorkoutRequest workoutRequest);
}
