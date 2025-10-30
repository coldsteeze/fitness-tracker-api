package com.fitness.tracker.fitness_tracker_api.service.impl;

import com.fitness.tracker.fitness_tracker_api.dto.request.LoginRequest;
import com.fitness.tracker.fitness_tracker_api.dto.request.RefreshTokenRequest;
import com.fitness.tracker.fitness_tracker_api.dto.request.RegisterRequest;
import com.fitness.tracker.fitness_tracker_api.dto.response.JwtResponse;
import com.fitness.tracker.fitness_tracker_api.entity.RefreshToken;
import com.fitness.tracker.fitness_tracker_api.entity.User;
import com.fitness.tracker.fitness_tracker_api.mapper.UserMapper;
import com.fitness.tracker.fitness_tracker_api.repository.UserRepository;
import com.fitness.tracker.fitness_tracker_api.security.JwtService;
import com.fitness.tracker.fitness_tracker_api.security.UserDetailsImpl;
import com.fitness.tracker.fitness_tracker_api.service.AuthService;
import com.fitness.tracker.fitness_tracker_api.service.RefreshTokenService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    private final RefreshTokenService refreshTokenService;


    @Override
    @Transactional
    public JwtResponse register(RegisterRequest registerRequest) {
        if (userRepository.findByUsername(registerRequest.getUsername()).isPresent()) {
            log.warn("Registration failed: username {} is already taken", registerRequest.getUsername());
            throw new RuntimeException("Registration failed: username is already taken");
        }

        if (userRepository.findByEmail(registerRequest.getEmail()).isPresent()) {
            log.warn("Registration failed: email {} is already taken", registerRequest.getEmail());
            throw new RuntimeException("Registration failed: email is already taken");
        }

        User user = userMapper.toEntity(registerRequest);
        user.setPassword(passwordEncoder.encode(registerRequest.getPassword()));
        userRepository.save(user);

        log.info("User registered successfully: userId: {}, username: {}, email: {}",
                user.getId(),
                registerRequest.getUsername(),
                registerRequest.getEmail());

        return generateTokens(user);
    }

    @Override
    @Transactional
    public JwtResponse login(LoginRequest loginRequest) {
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(loginRequest.getIdentifier(),
                            loginRequest.getPassword())
            );

            UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
            User user = userDetails.getUser();
            user.setLoggedAt(LocalDateTime.now());
            userRepository.save(user);

            log.info("User logged in: userId={}, email={}", user.getId(), user.getEmail());

            return generateTokens(user);
        } catch (BadCredentialsException e) {
            log.warn("Invalid login attempt for identifier: {}", loginRequest.getIdentifier());
            throw new RuntimeException("Invalid identifier or password");
        } catch (Exception e) {
            log.error("Unexpected login error for identifier: {}", loginRequest.getIdentifier(), e);
            throw new RuntimeException("Login failed", e);
        }
    }

    @Override
    public JwtResponse refreshTokens(RefreshTokenRequest refreshTokenRequest) {
        RefreshToken newRefreshToken = refreshTokenService.rotateRefreshToken(refreshTokenRequest.getRefreshToken());
        User user = newRefreshToken.getUser();
        String accessToken = jwtService.generateAccessToken(user.getId(), user.getUsername());

        return new JwtResponse(accessToken, newRefreshToken.getToken());
    }

    @Override
    public void logout(RefreshTokenRequest refreshTokenRequest) {
        refreshTokenService.deleteRefreshToken(refreshTokenRequest.getRefreshToken());
    }

    private JwtResponse generateTokens(User user) {
        String accessToken = jwtService.generateAccessToken(user.getId(), user.getUsername());
        String refreshToken = refreshTokenService.createRefreshToken(user).getToken();

        return new JwtResponse(accessToken, refreshToken);
    }
}
