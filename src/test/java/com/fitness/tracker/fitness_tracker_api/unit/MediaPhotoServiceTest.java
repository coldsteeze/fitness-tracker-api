package com.fitness.tracker.fitness_tracker_api.unit;

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
import com.fitness.tracker.fitness_tracker_api.service.impl.MediaPhotoServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class MediaPhotoServiceTest {

    @Mock
    private MediaPhotoRepository mediaPhotoRepository;

    @Mock
    private WorkoutRepository workoutRepository;

    @Mock
    private MediaPhotoMapper mediaPhotoMapper;

    @InjectMocks
    private MediaPhotoServiceImpl mediaPhotoService;

    private User user;
    private Workout workout;
    private MediaPhoto mediaPhoto;
    private MediaPhotoResponse mediaPhotoResponse;
    byte[] fileContent;

    @BeforeEach
    public void setUp() {
        user = new User();
        user.setId(1L);

        workout = new Workout();
        workout.setId(1L);
        workout.setUser(user);

        mediaPhoto = new MediaPhoto();
        mediaPhoto.setId(1L);
        mediaPhoto.setWorkout(workout);

        mediaPhotoResponse = new MediaPhotoResponse(1L, "file_name", LocalDate.now());

        fileContent = new byte[] {1,2,3};
    }

    @Test
    void uploadPhoto_returnsMediaPhotoResponse() {
        when(workoutRepository.findByIdAndUser(workout.getId(), user))
                .thenReturn(Optional.of(workout));
        when(mediaPhotoRepository.save(any(MediaPhoto.class))).thenReturn(mediaPhoto);
        when(mediaPhotoMapper.toDto(any(MediaPhoto.class))).thenReturn(mediaPhotoResponse);

        MediaPhotoResponse result = mediaPhotoService.uploadPhoto(
                workout.getId(),
                user,
                "file_name",
                fileContent
        );

        assertEquals(mediaPhotoResponse, result);
        verify(workoutRepository).findByIdAndUser(workout.getId(), user);
        verify(mediaPhotoRepository).save(any(MediaPhoto.class));
        verify(mediaPhotoMapper).toDto(any(MediaPhoto.class));
        verifyNoMoreInteractions(workoutRepository, mediaPhotoRepository, mediaPhotoMapper);
    }

    @Test
    void uploadPhoto_throwsEmptyFileException_whenDataIsEmpty() {
        byte[] emptyData = new byte[0];

        assertThrows(EmptyFileException.class,
                () -> mediaPhotoService.uploadPhoto(
                        workout.getId(),
                        user,
                        "file_name",
                        emptyData
                )
        );
    }

    @Test
    void uploadPhoto_shouldThrowIfWorkoutNotFound() {
        when(workoutRepository.findByIdAndUser(1L, user)).thenReturn(Optional.empty());
        assertThrows(WorkoutNotFoundException.class,
                () -> mediaPhotoService.uploadPhoto(
                        workout.getId(),
                        user,
                        "file_name",
                        fileContent
                ));
    }

    @Test
    void getPhotoById_returnsMediaPhoto() {
        when(mediaPhotoRepository.findById(any(Long.class))).thenReturn(Optional.of(mediaPhoto));

        MediaPhoto result = mediaPhotoService.getPhotoById(mediaPhoto.getId(), user);

        assertEquals(mediaPhoto, result);
        verify(mediaPhotoRepository).findById(any(Long.class));
        verifyNoMoreInteractions(mediaPhotoRepository);
    }

    @Test
    void getPhotoById_throwsAccessDeniedIfUserNotOwner() {
        User anotherUser = new User();
        anotherUser.setId(2L);

        when(mediaPhotoRepository.findById(any(Long.class))).thenReturn(Optional.of(mediaPhoto));

        assertThrows(MediaPhotoAccessDeniedException.class,
                () -> mediaPhotoService.getPhotoById(mediaPhoto.getId(), anotherUser)
        );

        verify(mediaPhotoRepository).findById(any(Long.class));

        verifyNoMoreInteractions(mediaPhotoRepository);
    }
}
