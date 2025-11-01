package com.fitness.tracker.fitness_tracker_api.unit;

import com.fitness.tracker.fitness_tracker_api.dto.response.JwtResponse;
import com.fitness.tracker.fitness_tracker_api.entity.RefreshToken;
import com.fitness.tracker.fitness_tracker_api.entity.User;
import com.fitness.tracker.fitness_tracker_api.exception.token.RefreshTokenExpiredException;
import com.fitness.tracker.fitness_tracker_api.exception.token.RefreshTokenNotFoundException;
import com.fitness.tracker.fitness_tracker_api.repository.RefreshTokenRepository;
import com.fitness.tracker.fitness_tracker_api.security.jwt.JwtProperties;
import com.fitness.tracker.fitness_tracker_api.security.jwt.JwtService;
import com.fitness.tracker.fitness_tracker_api.service.impl.RefreshTokenServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Duration;
import java.time.Instant;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class RefreshTokenServiceTest {

    @Mock
    private JwtService jwtService;

    @Mock
    private JwtProperties jwtProperties;

    @Mock
    private RefreshTokenRepository refreshTokenRepository;

    @InjectMocks
    private RefreshTokenServiceImpl refreshTokenService;

    private User user;
    private RefreshToken token;

    @BeforeEach
    void setUp() {
        user = new User();
        user.setId(1L);
        user.setUsername("john");

        token = RefreshToken.builder()
                .user(user)
                .token("old-token")
                .expiresAt(Instant.now().plus(Duration.ofHours(1)))
                .build();
    }

    @Test
    void rotateRefreshToken_shouldThrowIfTokenNotFound() {
        when(refreshTokenRepository.findByToken("old-token")).thenReturn(Optional.empty());

        assertThrows(RefreshTokenNotFoundException.class,
                () -> refreshTokenService.rotateRefreshToken("old-token"));

        verify(refreshTokenRepository, never()).delete(any());
    }

    @Test
    void rotateRefreshToken_shouldThrowIfTokenExpired() {
        token.setExpiresAt(Instant.now().minusSeconds(10));
        when(refreshTokenRepository.findByToken("old-token")).thenReturn(Optional.of(token));
        when(jwtService.isTokenExpired(token)).thenReturn(true);

        assertThrows(RefreshTokenExpiredException.class,
                () -> refreshTokenService.rotateRefreshToken("old-token"));

        verify(refreshTokenRepository).delete(token);
    }

    @Test
    void rotateRefreshToken_shouldDeleteOldAndCreateNewToken() {
        RefreshToken newToken = new RefreshToken();
        when(refreshTokenRepository.findByToken("old-token")).thenReturn(Optional.of(token));
        when(jwtService.isTokenExpired(token)).thenReturn(false);
        when(refreshTokenRepository.save(any())).thenReturn(newToken);
        when(jwtService.generateRefreshToken(user.getId(), user.getUsername())).thenReturn("new-token");
        when(jwtProperties.getRefreshTokenExpirationHours()).thenReturn(1L);

        RefreshToken result = refreshTokenService.rotateRefreshToken("old-token");

        assertNotNull(result);
        assertEquals(user, result.getUser());
        verify(refreshTokenRepository).delete(token);
        verify(refreshTokenRepository).save(any());
    }

    @Test
    void deleteRefreshToken_shouldDeleteToken() {
        when(refreshTokenRepository.findByToken("some-token")).thenReturn(Optional.of(token));

        refreshTokenService.deleteRefreshToken("some-token");

        verify(refreshTokenRepository).delete(token);
    }

    @Test
    void deleteRefreshToken_shouldThrowIfNotFound() {
        when(refreshTokenRepository.findByToken("some-token")).thenReturn(Optional.empty());

        assertThrows(RefreshTokenNotFoundException.class,
                () -> refreshTokenService.deleteRefreshToken("some-token"));
    }

    @Test
    void generateTokens_shouldReturnJwtResponse() {
        when(jwtService.generateAccessToken(user.getId(), user.getUsername())).thenReturn("access-token");
        when(jwtService.generateRefreshToken(user.getId(), user.getUsername())).thenReturn("refresh-token");
        when(jwtProperties.getRefreshTokenExpirationHours()).thenReturn(1L);
        when(refreshTokenRepository.save(any())).thenAnswer(invocation -> invocation.getArgument(0));

        JwtResponse response = refreshTokenService.generateTokens(user);

        assertEquals("access-token", response.accessToken());
        assertEquals("refresh-token", response.refreshToken());
    }
}

