package com.fitness.tracker.fitness_tracker_api.service;

import com.fitness.tracker.fitness_tracker_api.dto.response.MediaPhotoResponse;
import com.fitness.tracker.fitness_tracker_api.entity.MediaPhoto;
import com.fitness.tracker.fitness_tracker_api.entity.User;

public interface MediaPhotoService {

    MediaPhotoResponse uploadPhoto(Long workoutId, User user, String fileName, byte[] data);

    MediaPhoto getPhotoById(Long id, User user);
}
