package com.fitness.tracker.fitness_tracker_api.unit.fixtures;

import com.fitness.tracker.fitness_tracker_api.entity.RefreshToken;
import com.fitness.tracker.fitness_tracker_api.entity.User;
import lombok.experimental.UtilityClass;

import java.time.Duration;
import java.time.Instant;

@UtilityClass
public class RefreshTokenFixtures {

    private static final Instant FIXED = Instant.parse("2025-12-01T00:00:00Z");

    public static RefreshToken refreshToken(User user) {
        return RefreshToken.builder()
                .user(user)
                .token(AuthRequestFixtures.OLD_TOKEN)
                .expiresAt(FIXED.plus(Duration.ofHours(1)))
                .build();
    }

    public static RefreshToken emptyToken(User user) {
        return RefreshToken.builder()
                .user(user)
                .token(AuthRequestFixtures.NEW_TOKEN)
                .expiresAt(FIXED.plus(Duration.ofHours(1)))
                .build();
    }
}
