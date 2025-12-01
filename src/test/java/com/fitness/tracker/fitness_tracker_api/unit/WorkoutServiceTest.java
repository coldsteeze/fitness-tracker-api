package com.fitness.tracker.fitness_tracker_api.unit;

import com.fitness.tracker.fitness_tracker_api.dto.request.WorkoutFilterRequest;
import com.fitness.tracker.fitness_tracker_api.dto.request.WorkoutRequest;
import com.fitness.tracker.fitness_tracker_api.dto.response.PagedResponse;
import com.fitness.tracker.fitness_tracker_api.dto.response.WorkoutResponse;
import com.fitness.tracker.fitness_tracker_api.entity.User;
import com.fitness.tracker.fitness_tracker_api.entity.Workout;
import com.fitness.tracker.fitness_tracker_api.entity.enums.WorkoutType;
import com.fitness.tracker.fitness_tracker_api.exception.workout.WorkoutNotFoundException;
import com.fitness.tracker.fitness_tracker_api.mapper.WorkoutMapper;
import com.fitness.tracker.fitness_tracker_api.repository.WorkoutRepository;
import com.fitness.tracker.fitness_tracker_api.service.impl.WorkoutServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class WorkoutServiceTest {

    @Mock
    private WorkoutRepository workoutRepository;

    @Mock
    private WorkoutMapper workoutMapper;

    @InjectMocks
    private WorkoutServiceImpl workoutService;

    private User user;
    private Workout workout;
    private WorkoutRequest workoutRequest;
    private WorkoutResponse workoutResponse;

    @BeforeEach
    void setUp() {
        user = new User();
        user.setId(1L);

        workout = new Workout();
        workout.setId(1L);
        workout.setUser(user);

        workoutRequest = new WorkoutRequest();
        workoutRequest.setName("Morning Yoga");
        workoutRequest.setType(WorkoutType.YOGA);
        workoutRequest.setDate(LocalDate.now());
        workoutRequest.setDuration(60);
        workoutRequest.setCalories(200);
        workoutRequest.setNotes("Note");

        workoutResponse = new WorkoutResponse(
                1L, "Morning Yoga", WorkoutType.YOGA,
                LocalDate.now(), 60, 500, "Note"
        );
    }

    @Test
    void findById_whenWorkoutExists_returnsMappedResponse() {
        when(workoutRepository.findByIdAndUser(1L, user)).thenReturn(Optional.of(workout));
        when(workoutMapper.toDto(workout)).thenReturn(workoutResponse);

        WorkoutResponse result = workoutService.findById(1L, user);

        assertEquals(workoutResponse, result);
        verify(workoutRepository).findByIdAndUser(1L, user);
        verify(workoutMapper).toDto(workout);
        verifyNoMoreInteractions(workoutRepository, workoutMapper);
    }

    @Test
    void findById_whenNotFound_throwsException() {
        when(workoutRepository.findByIdAndUser(1L, user)).thenReturn(Optional.empty());
        assertThrows(WorkoutNotFoundException.class, () -> workoutService.findById(1L, user));
    }

    @Test
    void createWorkout_whenValidRequest_savesAndReturnsDto() {
        when(workoutMapper.toEntity(workoutRequest)).thenReturn(workout);
        when(workoutRepository.save(workout)).thenReturn(workout);
        when(workoutMapper.toDto(workout)).thenReturn(workoutResponse);

        WorkoutResponse result = workoutService.createWorkout(workoutRequest, user);

        assertEquals(workoutResponse, result);
        verify(workoutRepository).save(workout);
        verify(workoutMapper).toEntity(workoutRequest);
        verify(workoutMapper).toDto(workout);
        verifyNoMoreInteractions(workoutRepository, workoutMapper);
    }

    @Test
    void updateWorkout_whenWorkoutExists_updatesEntityAndReturnsDto() {
        when(workoutRepository.findByIdAndUser(1L, user)).thenReturn(Optional.of(workout));
        doNothing().when(workoutMapper).updateEntityFromDto(workout, workoutRequest);
        when(workoutRepository.save(workout)).thenReturn(workout);
        when(workoutMapper.toDto(workout)).thenReturn(workoutResponse);

        WorkoutResponse result = workoutService.updateWorkout(1L, workoutRequest, user);

        assertEquals(workoutResponse, result);
        verify(workoutRepository).findByIdAndUser(1L, user);
        verify(workoutMapper).updateEntityFromDto(workout, workoutRequest);
        verify(workoutRepository).save(workout);
        verify(workoutMapper).toDto(workout);
        verifyNoMoreInteractions(workoutRepository, workoutMapper);
    }

    @Test
    void updateWorkout_whenNotFound_throwsException() {
        when(workoutRepository.findByIdAndUser(1L, user)).thenReturn(Optional.empty());
        assertThrows(WorkoutNotFoundException.class,
                () -> workoutService.updateWorkout(1L, workoutRequest, user));
    }

    @Test
    void deleteWorkout_whenExists_deletesWorkout() {
        when(workoutRepository.findByIdAndUser(1L, user)).thenReturn(Optional.of(workout));
        doNothing().when(workoutRepository).delete(workout);

        workoutService.deleteWorkout(1L, user);

        verify(workoutRepository).findByIdAndUser(1L, user);
        verify(workoutRepository).delete(workout);
        verifyNoMoreInteractions(workoutRepository);
    }

    @Test
    void deleteWorkout_whenNotFound_throwsException() {
        when(workoutRepository.findByIdAndUser(1L, user)).thenReturn(Optional.empty());
        assertThrows(WorkoutNotFoundException.class,
                () -> workoutService.deleteWorkout(1L, user));
    }

    @Test
    void findAllWorkouts_whenCalled_returnsPagedResponse() {
        Pageable pageable = PageRequest.of(0, 10, Sort.by("date").descending());
        Page<Workout> page = new PageImpl<>(List.of(workout), pageable, 1);

        when(workoutRepository.findAllByFilters(eq(user), any(WorkoutFilterRequest.class), eq(pageable)))
                .thenReturn(page);
        when(workoutMapper.toDto(workout)).thenReturn(workoutResponse);

        PagedResponse<WorkoutResponse> result =
                workoutService.findAllWorkouts(new WorkoutFilterRequest(), pageable, user);

        assertNotNull(result);
        assertEquals(1, result.content().size());
        assertEquals(workoutResponse, result.content().get(0));

        verify(workoutRepository).findAllByFilters(eq(user), any(WorkoutFilterRequest.class), eq(pageable));
        verify(workoutMapper).toDto(workout);
    }
}

