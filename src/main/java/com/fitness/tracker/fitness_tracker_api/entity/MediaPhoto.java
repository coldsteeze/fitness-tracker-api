package com.fitness.tracker.fitness_tracker_api.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "media_photos")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class MediaPhoto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "file_name", nullable = false)
    private String fileName;

    @Lob
    @Column(name = "data", nullable = false)
    private byte[] data;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "workout_id", nullable = false)
    private Workout workout;
}
