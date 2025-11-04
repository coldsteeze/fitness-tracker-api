package com.fitness.tracker.fitness_tracker_api.mapper;

import com.fitness.tracker.fitness_tracker_api.dto.response.MediaPhotoResponse;
import com.fitness.tracker.fitness_tracker_api.entity.MediaPhoto;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface MediaPhotoMapper {

    MediaPhotoResponse toDto(MediaPhoto mediaPhoto);
}
