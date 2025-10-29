package com.fitness.tracker.fitness_tracker_api.security;


import com.auth0.jwt.exceptions.JWTVerificationException;

public interface JwtService {

    String getIdentifierFromToken(String token) throws JWTVerificationException;

    boolean isAccessToken(String token) throws JWTVerificationException;

    String generateAccessToken(Long userId, String identifier);

    String generateRefreshToken(Long userId, String identifier);
}
