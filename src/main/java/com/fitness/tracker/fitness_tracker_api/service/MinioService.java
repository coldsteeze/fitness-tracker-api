package com.fitness.tracker.fitness_tracker_api.service;

import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;

public interface MinioService {

    String upload(MultipartFile multipartFile);

    InputStream download(String objectKey);
}
