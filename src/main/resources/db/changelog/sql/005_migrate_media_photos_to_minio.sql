ALTER TABLE media_photos
    ADD COLUMN object_key VARCHAR(255),
    ADD COLUMN content_type VARCHAR(100),
    ADD COLUMN size BIGINT,
    DROP COLUMN data;