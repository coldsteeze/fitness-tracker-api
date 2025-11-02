CREATE TABLE workouts
(
    id         BIGSERIAL PRIMARY KEY,
    user_id    BIGINT       NOT NULL,
    name       VARCHAR(100) NOT NULL,
    type       VARCHAR(20)  NOT NULL,
    date       DATE         NOT NULL,
    duration   INTEGER      NOT NULL,
    calories   INTEGER      NOT NULL,
    notes      VARCHAR(500),
    created_at TIMESTAMP    NOT NULL,
    updated_at TIMESTAMP,
    CONSTRAINT fk_workouts_user FOREIGN KEY (user_id) REFERENCES users (id)
)