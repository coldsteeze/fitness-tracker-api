package com.fitness.tracker.fitness_tracker_api.service.impl;

import com.fitness.tracker.fitness_tracker_api.dto.response.MediaPhotoResponse;
import com.fitness.tracker.fitness_tracker_api.entity.MediaPhoto;
import com.fitness.tracker.fitness_tracker_api.entity.User;
import com.fitness.tracker.fitness_tracker_api.entity.Workout;
import com.fitness.tracker.fitness_tracker_api.exception.workout.WorkoutNotFoundException;
import com.fitness.tracker.fitness_tracker_api.mapper.MediaPhotoMapper;
import com.fitness.tracker.fitness_tracker_api.repository.MediaPhotoRepository;
import com.fitness.tracker.fitness_tracker_api.repository.WorkoutRepository;
import com.fitness.tracker.fitness_tracker_api.service.MediaPhotoService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
public class MediaPhotoServiceImpl implements MediaPhotoService {

    private final MediaPhotoRepository mediaPhotoRepository;
    private final WorkoutRepository workoutRepository;
    private final MediaPhotoMapper mediaPhotoMapper;

    @Override
    @Transactional
    public MediaPhotoResponse uploadPhoto(Long workoutId, User user, String fileName, byte[] data) {
        if (data == null || data.length == 0) {
            throw new IllegalArgumentException("File must not be empty");
        }

        Workout workout = workoutRepository.findByIdAndUser(workoutId, user)
                .orElseThrow(() -> new WorkoutNotFoundException(workoutId));

        MediaPhoto mediaPhoto = new MediaPhoto();
        mediaPhoto.setFileName(fileName);
        mediaPhoto.setData(data);
        mediaPhoto.setWorkout(workout);

        mediaPhotoRepository.save(mediaPhoto);
        log.info("Media Photo has been uploaded successfully userId: {}, workoutId: {}",
                user.getId(), workout.getId());

        return mediaPhotoMapper.toDto(mediaPhoto);
    }

    @Override
    @Transactional(readOnly = true)
    public MediaPhoto getPhotoById(Long id, User user) {
        MediaPhoto mediaPhoto = mediaPhotoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Photo not found"));

        Workout workout = mediaPhoto.getWorkout();
        if (!workout.getUser().getId().equals(user.getId())) {
            throw new RuntimeException("You do not have permission to view this photo");
        }

        log.info("Media Photo has been retrieved successfully userId: {}, workoutId: {}",
                user.getId(), workout.getId());

        return mediaPhoto;
    }
}
