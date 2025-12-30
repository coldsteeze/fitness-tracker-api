package com.fitness.tracker.fitness_tracker_api.service.impl;

import com.fitness.tracker.fitness_tracker_api.dto.response.MediaPhotoDownload;
import com.fitness.tracker.fitness_tracker_api.dto.response.MediaPhotoResponse;
import com.fitness.tracker.fitness_tracker_api.entity.MediaPhoto;
import com.fitness.tracker.fitness_tracker_api.entity.User;
import com.fitness.tracker.fitness_tracker_api.entity.Workout;
import com.fitness.tracker.fitness_tracker_api.exception.ErrorCode;
import com.fitness.tracker.fitness_tracker_api.exception.media.EmptyFileException;
import com.fitness.tracker.fitness_tracker_api.exception.media.MediaPhotoAccessDeniedException;
import com.fitness.tracker.fitness_tracker_api.exception.media.MediaPhotoNotFoundException;
import com.fitness.tracker.fitness_tracker_api.exception.workout.WorkoutNotFoundException;
import com.fitness.tracker.fitness_tracker_api.mapper.MediaPhotoMapper;
import com.fitness.tracker.fitness_tracker_api.repository.MediaPhotoRepository;
import com.fitness.tracker.fitness_tracker_api.repository.WorkoutRepository;
import com.fitness.tracker.fitness_tracker_api.service.MediaPhotoService;
import com.fitness.tracker.fitness_tracker_api.service.MinioService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;

@Service
@RequiredArgsConstructor
@Slf4j
public class MediaPhotoServiceImpl implements MediaPhotoService {

    private final MediaPhotoRepository mediaPhotoRepository;
    private final WorkoutRepository workoutRepository;
    private final MediaPhotoMapper mediaPhotoMapper;
    private final MinioService minioService;

    @Override
    @Transactional
    public MediaPhotoResponse uploadPhoto(Long workoutId, User user, MultipartFile file) {
        if (file == null || file.isEmpty()) {
            throw new EmptyFileException(ErrorCode.EMPTY_FILE);
        }

        Workout workout = workoutRepository.findByIdAndUser(workoutId, user)
                .orElseThrow(() -> new WorkoutNotFoundException(workoutId));

        String objectKey = minioService.upload(file);

        MediaPhoto mediaPhoto = MediaPhoto.builder()
                .fileName(file.getOriginalFilename())
                .contentType(file.getContentType())
                .size(file.getSize())
                .objectKey(objectKey)
                .workout(workout)
                .build();

        mediaPhotoRepository.save(mediaPhoto);
        log.info("Media Photo has been uploaded successfully userId: {}, workoutId: {}",
                user.getId(), workout.getId());

        return mediaPhotoMapper.toDto(mediaPhoto);
    }

    @Override
    @Transactional(readOnly = true)
    public MediaPhotoDownload getPhotoById(Long id, User user) {
        MediaPhoto mediaPhoto = mediaPhotoRepository.findById(id)
                .orElseThrow(() -> new MediaPhotoNotFoundException(ErrorCode.MEDIA_PHOTO_NOT_FOUND));

        Workout workout = mediaPhoto.getWorkout();
        if (!workout.getUser().getId().equals(user.getId())) {
            throw new MediaPhotoAccessDeniedException(ErrorCode.MEDIA_PHOTO_ACCESS_DENIED);
        }

        InputStream inputStream = minioService.download(mediaPhoto.getObjectKey());

        log.info("Media Photo has been retrieved successfully userId: {}, workoutId: {}",
                user.getId(), workout.getId());

        return new MediaPhotoDownload(
                inputStream,
                mediaPhoto.getContentType(),
                mediaPhoto.getSize()
        );
    }
}
