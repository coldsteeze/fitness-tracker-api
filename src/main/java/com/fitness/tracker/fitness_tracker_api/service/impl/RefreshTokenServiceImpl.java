package com.fitness.tracker.fitness_tracker_api.service.impl;

import com.fitness.tracker.fitness_tracker_api.entity.RefreshToken;
import com.fitness.tracker.fitness_tracker_api.entity.User;
import com.fitness.tracker.fitness_tracker_api.repository.RefreshTokenRepository;
import com.fitness.tracker.fitness_tracker_api.security.JwtProperties;
import com.fitness.tracker.fitness_tracker_api.security.JwtService;
import com.fitness.tracker.fitness_tracker_api.service.RefreshTokenService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.Instant;

@Service
@RequiredArgsConstructor
public class RefreshTokenServiceImpl implements RefreshTokenService {

    private final JwtService jwtService;
    private final JwtProperties jwtProperties;
    private final RefreshTokenRepository refreshTokenRepository;

    @Override
    @Transactional
    public RefreshToken createRefreshToken(User user) {
        String token = jwtService.generateRefreshToken(user.getId(), user.getUsername());

        RefreshToken refreshToken = RefreshToken.builder()
                .user(user)
                .token(token)
                .expiresAt(Instant.now().plus(Duration.ofHours(jwtProperties.getRefreshTokenExpirationHours())))
                .build();

        refreshTokenRepository.save(refreshToken);

        return refreshToken;
    }

    @Override
    @Transactional
    public RefreshToken rotateRefreshToken(String oldRefreshToken) {
        RefreshToken refreshToken = refreshTokenRepository.findByToken(oldRefreshToken)
                .orElseThrow(() -> new RuntimeException("Refresh token not found"));

        if (jwtService.isTokenExpired(refreshToken)) {
            refreshTokenRepository.delete(refreshToken);
            throw new RuntimeException("Refresh token is expired");
        }

        refreshTokenRepository.delete(refreshToken);

        return createRefreshToken(refreshToken.getUser());
    }

    @Override
    @Transactional
    public void deleteRefreshToken(String refreshToken) {
        RefreshToken token = refreshTokenRepository.findByToken(refreshToken)
                .orElseThrow(() -> new RuntimeException("Refresh token not found"));

        refreshTokenRepository.delete(token);
    }
}
