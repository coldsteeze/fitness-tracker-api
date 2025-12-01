package com.fitness.tracker.fitness_tracker_api.unit;

import com.fitness.tracker.fitness_tracker_api.dto.request.LoginRequest;
import com.fitness.tracker.fitness_tracker_api.dto.request.RefreshTokenRequest;
import com.fitness.tracker.fitness_tracker_api.dto.request.RegisterRequest;
import com.fitness.tracker.fitness_tracker_api.dto.response.JwtResponse;
import com.fitness.tracker.fitness_tracker_api.entity.RefreshToken;
import com.fitness.tracker.fitness_tracker_api.entity.User;
import com.fitness.tracker.fitness_tracker_api.exception.auth.EmailAlreadyExistsException;
import com.fitness.tracker.fitness_tracker_api.exception.auth.InvalidCredentialsException;
import com.fitness.tracker.fitness_tracker_api.exception.auth.UsernameAlreadyExistsException;
import com.fitness.tracker.fitness_tracker_api.mapper.UserMapper;
import com.fitness.tracker.fitness_tracker_api.repository.UserRepository;
import com.fitness.tracker.fitness_tracker_api.security.jwt.JwtService;
import com.fitness.tracker.fitness_tracker_api.security.user.UserDetailsImpl;
import com.fitness.tracker.fitness_tracker_api.service.RefreshTokenService;
import com.fitness.tracker.fitness_tracker_api.service.impl.AuthServiceImpl;
import com.fitness.tracker.fitness_tracker_api.unit.fixtures.AuthRequestFixtures;
import com.fitness.tracker.fitness_tracker_api.unit.fixtures.UserFixtures;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuthServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private UserMapper userMapper;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private JwtService jwtService;

    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    private RefreshTokenService refreshTokenService;

    @InjectMocks
    private AuthServiceImpl authService;

    @Test
    void register_shouldThrowUsernameAlreadyExists() {
        RegisterRequest request = AuthRequestFixtures.registerRequest();
        when(userRepository.findByUsername(AuthRequestFixtures.USERNAME))
                .thenReturn(Optional.of(new User()));
        assertThrows(UsernameAlreadyExistsException.class, () -> authService.register(request));
        verify(userRepository, never()).save(any());
    }

    @Test
    void register_shouldThrowEmailAlreadyExists() {
        RegisterRequest request = AuthRequestFixtures.registerRequest();
        when(userRepository.findByUsername(AuthRequestFixtures.USERNAME)).thenReturn(Optional.empty());
        when(userRepository.findByEmail(AuthRequestFixtures.EMAIL)).thenReturn(Optional.of(new User()));
        assertThrows(EmailAlreadyExistsException.class, () -> authService.register(request));
        verify(userRepository, never()).save(any());
    }

    @Test
    void register_shouldSaveUserAndReturnTokens() {
        RegisterRequest request = AuthRequestFixtures.registerRequest();
        User user = new User();
        JwtResponse jwtResponse = new JwtResponse(AuthRequestFixtures.ACCESS_TOKEN, AuthRequestFixtures.REFRESH_TOKEN);

        when(userRepository.findByUsername(AuthRequestFixtures.USERNAME)).thenReturn(Optional.empty());
        when(userRepository.findByEmail(AuthRequestFixtures.EMAIL)).thenReturn(Optional.empty());
        when(userMapper.toEntity(request)).thenReturn(user);
        when(passwordEncoder.encode(AuthRequestFixtures.PASSWORD)).thenReturn(AuthRequestFixtures.ENCODED_PASSWORD);
        when(refreshTokenService.generateTokens(user)).thenReturn(jwtResponse);

        JwtResponse response = authService.register(request);

        assertEquals(AuthRequestFixtures.ACCESS_TOKEN, response.accessToken());
        assertEquals(AuthRequestFixtures.REFRESH_TOKEN, response.refreshToken());
        assertEquals(AuthRequestFixtures.ENCODED_PASSWORD, user.getPassword());
        verify(userRepository).save(user);
    }

    @Test
    void login_shouldAuthenticateAndReturnTokens() {
        LoginRequest request = AuthRequestFixtures.loginRequest();
        User user = UserFixtures.user();

        Authentication auth = mock(Authentication.class);
        UserDetailsImpl userDetails = new UserDetailsImpl(user);
        JwtResponse jwtResponse = new JwtResponse(AuthRequestFixtures.ACCESS_TOKEN, AuthRequestFixtures.REFRESH_TOKEN);

        when(authenticationManager.authenticate(any())).thenReturn(auth);
        when(auth.getPrincipal()).thenReturn(userDetails);
        when(refreshTokenService.generateTokens(user)).thenReturn(jwtResponse);

        JwtResponse response = authService.login(request);

        assertEquals(AuthRequestFixtures.ACCESS_TOKEN, response.accessToken());
        assertEquals(AuthRequestFixtures.REFRESH_TOKEN, response.refreshToken());
        verify(userRepository).save(user);
    }

    @Test
    void login_shouldThrowInvalidCredentials() {
        LoginRequest request = AuthRequestFixtures.loginRequest();
        when(authenticationManager.authenticate(any())).thenThrow(new BadCredentialsException("bad creds"));

        assertThrows(InvalidCredentialsException.class, () -> authService.login(request));
        verify(userRepository, never()).save(any());
    }

    @Test
    void refreshTokens_shouldReturnNewAccessToken() {
        User user = new User();
        RefreshToken refreshToken = new RefreshToken();
        refreshToken.setUser(user);
        when(refreshTokenService.rotateRefreshToken(AuthRequestFixtures.OLD_TOKEN)).thenReturn(refreshToken);
        when(jwtService.generateAccessToken(user.getId(), user.getUsername())).thenReturn(AuthRequestFixtures.ACCESS_TOKEN);

        RefreshTokenRequest request = AuthRequestFixtures.refreshTokenRequest(AuthRequestFixtures.OLD_TOKEN);
        JwtResponse response = authService.refreshTokens(request);

        assertEquals(AuthRequestFixtures.ACCESS_TOKEN, response.accessToken());
        assertEquals(refreshToken.getToken(), response.refreshToken());
    }

    @Test
    void logout_shouldDeleteRefreshToken() {
        RefreshTokenRequest request = AuthRequestFixtures.refreshTokenRequest("some-token");
        authService.logout(request);
        verify(refreshTokenService).deleteRefreshToken("some-token");
    }
}


