package com.fitness.tracker.fitness_tracker_api.repository;

import com.fitness.tracker.fitness_tracker_api.entity.User;
import com.fitness.tracker.fitness_tracker_api.entity.Workout;
import com.fitness.tracker.fitness_tracker_api.entity.enums.WorkoutType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.Optional;

public interface WorkoutRepository extends JpaRepository<Workout, Long> {

    Optional<Workout> findByIdAndUser(Long id, User user);

    @Query("""
                SELECT w FROM Workout w
                WHERE w.user = :user
                  AND (COALESCE(:type, w.type) = w.type)
                  AND (w.date >= COALESCE(:start, w.date))
                  AND (w.date <= COALESCE(:end, w.date))
                  AND (w.duration >= COALESCE(:durationStart, w.duration))
                  AND (w.duration <= COALESCE(:durationEnd, w.duration))
            """)
    Page<Workout> findAllByFilters(
            @Param("user") User user,
            @Param("type") WorkoutType type,
            @Param("start") LocalDate start,
            @Param("end") LocalDate end,
            @Param("durationStart") Integer durationStart,
            @Param("durationEnd") Integer durationEnd,
            Pageable pageable
    );
}
