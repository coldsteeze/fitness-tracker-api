package com.fitness.tracker.fitness_tracker_api.service;

import com.fitness.tracker.fitness_tracker_api.dto.response.JwtResponse;
import com.fitness.tracker.fitness_tracker_api.entity.RefreshToken;
import com.fitness.tracker.fitness_tracker_api.entity.User;

public interface RefreshTokenService {

    RefreshToken rotateRefreshToken(String oldRefreshToken);

    void deleteRefreshToken(String refreshToken);

    JwtResponse generateTokens(User user);
}
