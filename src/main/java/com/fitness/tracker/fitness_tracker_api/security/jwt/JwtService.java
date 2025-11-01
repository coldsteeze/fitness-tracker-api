package com.fitness.tracker.fitness_tracker_api.security.jwt;


import com.auth0.jwt.exceptions.JWTVerificationException;
import com.fitness.tracker.fitness_tracker_api.entity.RefreshToken;

public interface JwtService {

    String getIdentifierFromToken(String token) throws JWTVerificationException;

    boolean isAccessToken(String token) throws JWTVerificationException;

    String generateAccessToken(Long userId, String identifier);

    String generateRefreshToken(Long userId, String identifier);

    boolean isTokenExpired(RefreshToken refreshToken);
}
