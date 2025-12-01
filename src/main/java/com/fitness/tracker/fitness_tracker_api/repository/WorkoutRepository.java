package com.fitness.tracker.fitness_tracker_api.repository;

import com.fitness.tracker.fitness_tracker_api.dto.request.WorkoutFilterRequest;
import com.fitness.tracker.fitness_tracker_api.entity.User;
import com.fitness.tracker.fitness_tracker_api.entity.Workout;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface WorkoutRepository extends JpaRepository<Workout, Long> {

    Optional<Workout> findByIdAndUser(Long id, User user);

    @Query("""
                SELECT w FROM Workout w
                WHERE w.user = :user
                  AND (:#{#f.type == null} = true OR w.type = :#{#f.type})
                  AND (:#{#f.dateStart == null} = true OR w.date >= :#{#f.dateStart})
                  AND (:#{#f.dateEnd == null} = true OR w.date <= :#{#f.dateEnd})
                  AND (:#{#f.durationStart == null} = true OR w.duration >= :#{#f.durationStart})
                  AND (:#{#f.durationEnd == null} = true OR w.duration <= :#{#f.durationEnd})
            """)
    Page<Workout> findAllByFilters(
            @Param("user") User user,
            @Param("f") WorkoutFilterRequest filter,
            Pageable pageable
    );
}
