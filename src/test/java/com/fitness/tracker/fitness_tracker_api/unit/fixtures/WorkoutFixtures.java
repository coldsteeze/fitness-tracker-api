package com.fitness.tracker.fitness_tracker_api.unit.fixtures;

import com.fitness.tracker.fitness_tracker_api.dto.request.WorkoutRequest;
import com.fitness.tracker.fitness_tracker_api.dto.response.WorkoutResponse;
import com.fitness.tracker.fitness_tracker_api.entity.User;
import com.fitness.tracker.fitness_tracker_api.entity.Workout;
import com.fitness.tracker.fitness_tracker_api.entity.enums.WorkoutType;
import lombok.experimental.UtilityClass;

import java.time.LocalDate;

@UtilityClass
public class WorkoutFixtures {

    public static final LocalDate DATE = LocalDate.of(2024, 1, 1);

    public static Workout workout(User user) {
        Workout workout = new Workout();
        workout.setId(1L);
        workout.setUser(user);
        workout.setName("Morning Yoga");
        workout.setType(WorkoutType.YOGA);
        workout.setDate(DATE);
        workout.setDuration(60);
        workout.setCalories(200);
        workout.setNotes("Note");

        return workout;
    }

    public static WorkoutRequest workoutRequest() {
        WorkoutRequest workoutRequest = new WorkoutRequest();
        workoutRequest.setName("Morning Yoga");
        workoutRequest.setType(WorkoutType.YOGA);
        workoutRequest.setDate(DATE);
        workoutRequest.setDuration(60);
        workoutRequest.setCalories(200);
        workoutRequest.setNotes("Note");

        return workoutRequest;
    }

    public static WorkoutResponse workoutResponse() {
        return new WorkoutResponse(
                1L, "Morning Yoga", WorkoutType.YOGA,
                DATE, 60, 200, "Note"
        );
    }
}
