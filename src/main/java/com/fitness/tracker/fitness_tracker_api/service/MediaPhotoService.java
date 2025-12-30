package com.fitness.tracker.fitness_tracker_api.service;

import com.fitness.tracker.fitness_tracker_api.dto.response.MediaPhotoDownload;
import com.fitness.tracker.fitness_tracker_api.dto.response.MediaPhotoResponse;
import com.fitness.tracker.fitness_tracker_api.entity.User;
import org.springframework.web.multipart.MultipartFile;

public interface MediaPhotoService {

    MediaPhotoResponse uploadPhoto(Long workoutId, User user, MultipartFile multipartFile);

    MediaPhotoDownload getPhotoById(Long id, User user);
}
