package com.fitness.tracker.fitness_tracker_api.security;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.Instant;
import java.util.Date;

@Service
public class JwtServiceImpl implements JwtService {

    private final JwtProperties jwtProperties;
    private final Algorithm algorithm;
    private final JWTVerifier verifier;

    public JwtServiceImpl(JwtProperties jwtProperties) {
        this.jwtProperties = jwtProperties;
        this.algorithm = Algorithm.HMAC256(jwtProperties.getSecret());
        this.verifier = JWT.require(algorithm)
                .withIssuer(jwtProperties.getIssuer())
                .build();
    }

    @Override
    public String getIdentifierFromToken(String token) throws JWTVerificationException {
        DecodedJWT jwt = verifier.verify(token);
        return jwt.getClaim("identifier").asString();
    }

    @Override
    public boolean isAccessToken(String token) throws JWTVerificationException {
        DecodedJWT jwt = verifier.verify(token);
        String type = jwt.getClaim("type").asString();
        return "access".equals(type);
    }

    @Override
    public String generateAccessToken(Long userId, String identifier) {
        Instant now = Instant.now();
        Instant expiresAt = now.plus(Duration.ofMinutes(jwtProperties.getAccessTokenExpirationMinutes()));

        return JWT.create()
                .withSubject(userId.toString())
                .withClaim("identifier", identifier)
                .withClaim("type", "access")
                .withIssuer(jwtProperties.getIssuer())
                .withIssuedAt(Date.from(now))
                .withExpiresAt(Date.from(expiresAt))
                .sign(algorithm);
    }

    @Override
    public String generateRefreshToken(Long userId, String identifier) {
        Instant now = Instant.now();
        Instant expiresAt = now.plus(Duration.ofHours(jwtProperties.getRefreshTokenExpirationHours()));

        return JWT.create()
                .withSubject(userId.toString())
                .withClaim("identifier", identifier)
                .withClaim("type", "refresh")
                .withIssuer(jwtProperties.getIssuer())
                .withIssuedAt(Date.from(now))
                .withExpiresAt(Date.from(expiresAt))
                .sign(algorithm);
    }
}
