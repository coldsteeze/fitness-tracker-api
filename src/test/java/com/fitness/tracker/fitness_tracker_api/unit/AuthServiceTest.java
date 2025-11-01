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

    private static final String USERNAME = "john";
    private static final String EMAIL = "john@example.com";
    private static final String PASSWORD = "password";
    private static final String ENCODED_PASSWORD = "encoded-password";
    private static final String ACCESS_TOKEN = "access-token";
    private static final String REFRESH_TOKEN = "refresh-token";

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

    private RegisterRequest createRegisterRequest() {
        RegisterRequest request = new RegisterRequest();
        request.setUsername(USERNAME);
        request.setEmail(EMAIL);
        request.setPassword(PASSWORD);
        return request;
    }

    private LoginRequest createLoginRequest() {
        LoginRequest request = new LoginRequest();
        request.setIdentifier(EMAIL);
        request.setPassword(PASSWORD);
        return request;
    }

    private RefreshTokenRequest createRefreshTokenRequest(String token) {
        RefreshTokenRequest request = new RefreshTokenRequest();
        request.setRefreshToken(token);
        return request;
    }

    @Test
    void register_shouldThrowUsernameAlreadyExists() {
        RegisterRequest request = createRegisterRequest();
        when(userRepository.findByUsername(USERNAME)).thenReturn(Optional.of(new User()));
        assertThrows(UsernameAlreadyExistsException.class, () -> authService.register(request));
        verify(userRepository, never()).save(any());
    }

    @Test
    void register_shouldThrowEmailAlreadyExists() {
        RegisterRequest request = createRegisterRequest();
        when(userRepository.findByUsername(USERNAME)).thenReturn(Optional.empty());
        when(userRepository.findByEmail(EMAIL)).thenReturn(Optional.of(new User()));
        assertThrows(EmailAlreadyExistsException.class, () -> authService.register(request));
        verify(userRepository, never()).save(any());
    }

    @Test
    void register_shouldSaveUserAndReturnTokens() {
        RegisterRequest request = createRegisterRequest();
        User user = new User();
        JwtResponse jwtResponse = new JwtResponse(ACCESS_TOKEN, REFRESH_TOKEN);

        when(userRepository.findByUsername(USERNAME)).thenReturn(Optional.empty());
        when(userRepository.findByEmail(EMAIL)).thenReturn(Optional.empty());
        when(userMapper.toEntity(request)).thenReturn(user);
        when(passwordEncoder.encode(PASSWORD)).thenReturn(ENCODED_PASSWORD);
        when(refreshTokenService.generateTokens(user)).thenReturn(jwtResponse);

        JwtResponse response = authService.register(request);

        assertEquals(ACCESS_TOKEN, response.accessToken());
        assertEquals(REFRESH_TOKEN, response.refreshToken());
        assertEquals(ENCODED_PASSWORD, user.getPassword());
        verify(userRepository).save(user);
    }

    @Test
    void login_shouldAuthenticateAndReturnTokens() {
        LoginRequest request = createLoginRequest();
        User user = new User();
        user.setId(1L);
        user.setEmail(EMAIL);

        Authentication auth = mock(Authentication.class);
        UserDetailsImpl userDetails = new UserDetailsImpl(user);
        JwtResponse jwtResponse = new JwtResponse(ACCESS_TOKEN, REFRESH_TOKEN);

        when(authenticationManager.authenticate(any())).thenReturn(auth);
        when(auth.getPrincipal()).thenReturn(userDetails);
        when(refreshTokenService.generateTokens(user)).thenReturn(jwtResponse);

        JwtResponse response = authService.login(request);

        assertEquals(ACCESS_TOKEN, response.accessToken());
        assertEquals(REFRESH_TOKEN, response.refreshToken());
        verify(userRepository).save(user);
    }

    @Test
    void login_shouldThrowInvalidCredentials() {
        LoginRequest request = createLoginRequest();
        when(authenticationManager.authenticate(any())).thenThrow(new BadCredentialsException("bad creds"));

        assertThrows(InvalidCredentialsException.class, () -> authService.login(request));
        verify(userRepository, never()).save(any());
    }

    @Test
    void refreshTokens_shouldReturnNewAccessToken() {
        User user = new User();
        RefreshToken refreshToken = new RefreshToken();
        refreshToken.setUser(user);
        when(refreshTokenService.rotateRefreshToken("old-token")).thenReturn(refreshToken);
        when(jwtService.generateAccessToken(user.getId(), user.getUsername())).thenReturn(ACCESS_TOKEN);

        RefreshTokenRequest request = createRefreshTokenRequest("old-token");
        JwtResponse response = authService.refreshTokens(request);

        assertEquals(ACCESS_TOKEN, response.accessToken());
        assertEquals(refreshToken.getToken(), response.refreshToken());
    }

    @Test
    void logout_shouldDeleteRefreshToken() {
        RefreshTokenRequest request = createRefreshTokenRequest("some-token");
        authService.logout(request);
        verify(refreshTokenService).deleteRefreshToken("some-token");
    }
}


