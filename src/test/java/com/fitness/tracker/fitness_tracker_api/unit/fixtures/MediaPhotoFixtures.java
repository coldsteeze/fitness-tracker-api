package com.fitness.tracker.fitness_tracker_api.unit.fixtures;

import com.fitness.tracker.fitness_tracker_api.dto.response.MediaPhotoResponse;
import com.fitness.tracker.fitness_tracker_api.entity.MediaPhoto;
import com.fitness.tracker.fitness_tracker_api.entity.Workout;
import lombok.experimental.UtilityClass;

import java.time.LocalDate;

@UtilityClass
public class MediaPhotoFixtures {

    private static final LocalDate DATE = LocalDate.parse("2025-12-01");
    public static final String FILE_NAME = "file_name";

    public static MediaPhoto mediaPhoto(Workout workout) {
        MediaPhoto mediaPhoto = new MediaPhoto();
        mediaPhoto.setId(1L);
        mediaPhoto.setWorkout(workout);

        return mediaPhoto;
    }

    public static MediaPhotoResponse mediaPhotoResponse() {
        return new MediaPhotoResponse(1L, FILE_NAME, DATE);
    }
}
