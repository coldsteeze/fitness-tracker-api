package com.fitness.tracker.fitness_tracker_api.unit;

import com.fitness.tracker.fitness_tracker_api.dto.response.MediaPhotoDownload;
import com.fitness.tracker.fitness_tracker_api.dto.response.MediaPhotoResponse;
import com.fitness.tracker.fitness_tracker_api.entity.MediaPhoto;
import com.fitness.tracker.fitness_tracker_api.entity.User;
import com.fitness.tracker.fitness_tracker_api.entity.Workout;
import com.fitness.tracker.fitness_tracker_api.exception.media.EmptyFileException;
import com.fitness.tracker.fitness_tracker_api.exception.media.MediaPhotoAccessDeniedException;
import com.fitness.tracker.fitness_tracker_api.exception.workout.WorkoutNotFoundException;
import com.fitness.tracker.fitness_tracker_api.mapper.MediaPhotoMapper;
import com.fitness.tracker.fitness_tracker_api.repository.MediaPhotoRepository;
import com.fitness.tracker.fitness_tracker_api.repository.WorkoutRepository;
import com.fitness.tracker.fitness_tracker_api.service.MinioService;
import com.fitness.tracker.fitness_tracker_api.service.impl.MediaPhotoServiceImpl;
import com.fitness.tracker.fitness_tracker_api.unit.fixtures.MediaPhotoFixtures;
import com.fitness.tracker.fitness_tracker_api.unit.fixtures.UserFixtures;
import com.fitness.tracker.fitness_tracker_api.unit.fixtures.WorkoutFixtures;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockMultipartFile;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class MediaPhotoServiceTest {

    @Mock
    private MediaPhotoRepository mediaPhotoRepository;

    @Mock
    private WorkoutRepository workoutRepository;

    @Mock
    private MediaPhotoMapper mediaPhotoMapper;

    @Mock
    private MinioService minioService;

    @InjectMocks
    private MediaPhotoServiceImpl mediaPhotoService;

    private User user;
    private Workout workout;
    private MediaPhoto mediaPhoto;
    private MediaPhotoResponse mediaPhotoResponse;
    private MockMultipartFile mockMultipartFile;
    private MockMultipartFile emptyMockMultipartFile;

    @BeforeEach
    public void setUp() {
        user = UserFixtures.user();
        workout = WorkoutFixtures.workout(user);
        mediaPhoto = MediaPhotoFixtures.mediaPhoto(workout);
        mediaPhotoResponse = MediaPhotoFixtures.mediaPhotoResponse();
        mockMultipartFile = MediaPhotoFixtures.mockMultipartFile();
        emptyMockMultipartFile = MediaPhotoFixtures.emptyMultipartFile();
    }

    @Test
    void uploadPhoto_returnsMediaPhotoResponse() {
        when(workoutRepository.findByIdAndUser(workout.getId(), user))
                .thenReturn(Optional.of(workout));
        when(minioService.upload(mockMultipartFile)).thenReturn(MediaPhotoFixtures.OBJECT_KEY);
        when(mediaPhotoRepository.save(any(MediaPhoto.class))).thenReturn(mediaPhoto);
        when(mediaPhotoMapper.toDto(any(MediaPhoto.class))).thenReturn(mediaPhotoResponse);

        MediaPhotoResponse result = mediaPhotoService.uploadPhoto(
                workout.getId(),
                user,
                mockMultipartFile
        );

        assertEquals(mediaPhotoResponse, result);
        verify(workoutRepository).findByIdAndUser(workout.getId(), user);
        verify(minioService).upload(mockMultipartFile);
        verify(mediaPhotoRepository).save(any(MediaPhoto.class));
        verify(mediaPhotoMapper).toDto(any(MediaPhoto.class));
        verifyNoMoreInteractions(workoutRepository, minioService, mediaPhotoRepository, mediaPhotoMapper);
    }

    @Test
    void uploadPhoto_throwsEmptyFileExceptionIfDataIsEmpty() {
        assertThrows(EmptyFileException.class,
                () -> mediaPhotoService.uploadPhoto(
                        workout.getId(),
                        user,
                        emptyMockMultipartFile
                )
        );
    }

    @Test
    void uploadPhoto_shouldThrowIfWorkoutNotFound() {
        when(workoutRepository.findByIdAndUser(workout.getId(), user)).thenReturn(Optional.empty());
        assertThrows(WorkoutNotFoundException.class,
                () -> mediaPhotoService.uploadPhoto(
                        workout.getId(),
                        user,
                        mockMultipartFile
                ));

        verify(workoutRepository).findByIdAndUser(workout.getId(), user);
        verifyNoInteractions(minioService, mediaPhotoRepository);
    }

    @Test
    void getPhotoById_returnsMediaPhoto() throws IOException {
        when(mediaPhotoRepository.findById(mediaPhoto.getId())).thenReturn(Optional.of(mediaPhoto));
        when(minioService.download(MediaPhotoFixtures.OBJECT_KEY))
                .thenReturn(new ByteArrayInputStream(MediaPhotoFixtures.CONTENT));

        MediaPhotoDownload result = mediaPhotoService.getPhotoById(mediaPhoto.getId(), user);

        assertEquals(mediaPhoto.getContentType(), result.contentType());
        assertEquals(mediaPhoto.getSize(), result.size());

        byte[] bytes = result.inputStream().readAllBytes();
        assertArrayEquals(MediaPhotoFixtures.CONTENT, bytes);
        verify(mediaPhotoRepository).findById(mediaPhoto.getId());
        verify(minioService).download((MediaPhotoFixtures.OBJECT_KEY));
        verifyNoMoreInteractions(mediaPhotoRepository, minioService);
    }

    @Test
    void getPhotoById_throwsAccessDeniedIfUserNotOwner() {
        User anotherUser = new User();
        anotherUser.setId(2L);

        when(mediaPhotoRepository.findById(mediaPhoto.getId())).thenReturn(Optional.of(mediaPhoto));

        assertThrows(MediaPhotoAccessDeniedException.class,
                () -> mediaPhotoService.getPhotoById(mediaPhoto.getId(), anotherUser)
        );

        verify(mediaPhotoRepository).findById(mediaPhoto.getId());

        verifyNoMoreInteractions(mediaPhotoRepository, minioService);
    }
}
