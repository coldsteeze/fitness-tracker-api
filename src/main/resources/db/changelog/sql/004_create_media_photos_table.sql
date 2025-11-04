CREATE TABLE media_photos
(
    id         BIGSERIAL PRIMARY KEY,
    file_name  VARCHAR(255) NOT NULL,
    data       BYTEA        NOT NULL,
    workout_id BIGINT       NOT NULL,
    CONSTRAINT fk_media_photos_workout FOREIGN KEY (workout_id) REFERENCES workouts (id)
)