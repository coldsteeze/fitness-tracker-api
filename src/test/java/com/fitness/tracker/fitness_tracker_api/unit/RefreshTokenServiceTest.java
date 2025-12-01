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
import com.fitness.tracker.fitness_tracker_api.unit.fixtures.AuthRequestFixtures;
import com.fitness.tracker.fitness_tracker_api.unit.fixtures.RefreshTokenFixtures;
import com.fitness.tracker.fitness_tracker_api.unit.fixtures.UserFixtures;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

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
        user = UserFixtures.user();
        token = RefreshTokenFixtures.refreshToken(user);
    }

    @Test
    void rotateRefreshToken_shouldThrowIfTokenNotFound() {
        when(refreshTokenRepository.findByToken(AuthRequestFixtures.OLD_TOKEN)).thenReturn(Optional.empty());

        assertThrows(RefreshTokenNotFoundException.class,
                () -> refreshTokenService.rotateRefreshToken(AuthRequestFixtures.OLD_TOKEN));

        verify(refreshTokenRepository, never()).delete(any());
    }

    @Test
    void rotateRefreshToken_shouldThrowIfTokenExpired() {
        token.setExpiresAt(Instant.now().minusSeconds(10));
        when(refreshTokenRepository.findByToken(AuthRequestFixtures.OLD_TOKEN)).thenReturn(Optional.of(token));
        when(jwtService.isTokenExpired(token)).thenReturn(true);

        assertThrows(RefreshTokenExpiredException.class,
                () -> refreshTokenService.rotateRefreshToken(AuthRequestFixtures.OLD_TOKEN));

        verify(refreshTokenRepository).delete(token);
    }

    @Test
    void rotateRefreshToken_shouldDeleteOldAndCreateNewToken() {
        RefreshToken newToken = RefreshTokenFixtures.emptyToken(user);
        when(refreshTokenRepository.findByToken(AuthRequestFixtures.OLD_TOKEN)).thenReturn(Optional.of(token));
        when(jwtService.isTokenExpired(token)).thenReturn(false);
        when(refreshTokenRepository.save(any())).thenReturn(newToken);
        when(jwtService.generateRefreshToken(user.getId(), user.getUsername())).thenReturn(AuthRequestFixtures.NEW_TOKEN);
        when(jwtProperties.getRefreshTokenExpirationHours()).thenReturn(1L);

        RefreshToken result = refreshTokenService.rotateRefreshToken(AuthRequestFixtures.OLD_TOKEN);

        ArgumentCaptor<RefreshToken> captor = ArgumentCaptor.forClass(RefreshToken.class);
        verify(refreshTokenRepository).save(captor.capture());
        RefreshToken saved = captor.getValue();

        assertNotNull(result);
        assertEquals(user, saved.getUser());
        assertEquals(AuthRequestFixtures.NEW_TOKEN, saved.getToken());
        assertNotNull(saved.getExpiresAt());

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
        when(jwtService.generateAccessToken(user.getId(), user.getUsername())).thenReturn(AuthRequestFixtures.ACCESS_TOKEN);
        when(jwtService.generateRefreshToken(user.getId(), user.getUsername())).thenReturn(AuthRequestFixtures.REFRESH_TOKEN);
        when(jwtProperties.getRefreshTokenExpirationHours()).thenReturn(1L);
        when(refreshTokenRepository.save(any())).thenAnswer(invocation -> invocation.getArgument(0));

        JwtResponse response = refreshTokenService.generateTokens(user);

        assertEquals(AuthRequestFixtures.ACCESS_TOKEN, response.accessToken());
        assertEquals(AuthRequestFixtures.REFRESH_TOKEN, response.refreshToken());
    }
}

