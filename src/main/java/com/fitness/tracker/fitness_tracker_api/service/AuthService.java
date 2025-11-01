package com.fitness.tracker.fitness_tracker_api.service;

import com.fitness.tracker.fitness_tracker_api.dto.request.LoginRequest;
import com.fitness.tracker.fitness_tracker_api.dto.request.RefreshTokenRequest;
import com.fitness.tracker.fitness_tracker_api.dto.request.RegisterRequest;
import com.fitness.tracker.fitness_tracker_api.dto.response.JwtResponse;

public interface AuthService {

    JwtResponse register(RegisterRequest registerRequest);

    JwtResponse login(LoginRequest loginRequest);

    JwtResponse refreshTokens(RefreshTokenRequest refreshTokenRequest);

    void logout(RefreshTokenRequest refreshTokenRequest);
}
