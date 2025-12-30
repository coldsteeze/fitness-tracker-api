package com.fitness.tracker.fitness_tracker_api.unit.fixtures;

import com.fitness.tracker.fitness_tracker_api.dto.response.MediaPhotoResponse;
import com.fitness.tracker.fitness_tracker_api.entity.MediaPhoto;
import com.fitness.tracker.fitness_tracker_api.entity.Workout;
import lombok.experimental.UtilityClass;
import org.springframework.mock.web.MockMultipartFile;

import java.time.LocalDate;

@UtilityClass
public class MediaPhotoFixtures {

    private static final LocalDate DATE = LocalDate.parse("2025-12-01");
    public static final String FILE_NAME = "file_name";
    public static final String CONTENT_TYPE = "image/png";
    public static final byte[] CONTENT = {1,2,3};
    public static final Long SIZE = (long) CONTENT.length;
    public static final String OBJECT_KEY = "OBJECT_KEY";

    public static MediaPhoto mediaPhoto(Workout workout) {
        MediaPhoto mediaPhoto = new MediaPhoto();
        mediaPhoto.setId(1L);
        mediaPhoto.setWorkout(workout);
        mediaPhoto.setObjectKey(OBJECT_KEY);
        mediaPhoto.setSize(SIZE);
        mediaPhoto.setContentType(CONTENT_TYPE);
        mediaPhoto.setFileName(FILE_NAME);

        return mediaPhoto;
    }

    public static MediaPhotoResponse mediaPhotoResponse() {
        return new MediaPhotoResponse(1L, FILE_NAME, DATE);
    }

    public static MockMultipartFile mockMultipartFile() {
        return new MockMultipartFile("file", FILE_NAME, CONTENT_TYPE, CONTENT);
    }

    public static MockMultipartFile emptyMultipartFile() {
        return new MockMultipartFile("file", FILE_NAME, CONTENT_TYPE, new byte[] {});
    }
}
