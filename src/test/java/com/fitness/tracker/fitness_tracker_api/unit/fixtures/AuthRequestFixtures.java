package com.fitness.tracker.fitness_tracker_api.unit.fixtures;

import com.fitness.tracker.fitness_tracker_api.dto.request.LoginRequest;
import com.fitness.tracker.fitness_tracker_api.dto.request.RefreshTokenRequest;
import com.fitness.tracker.fitness_tracker_api.dto.request.RegisterRequest;
import lombok.experimental.UtilityClass;

@UtilityClass
public class AuthRequestFixtures {

    public static final String USERNAME = "john";
    public static final String EMAIL = "john@example.com";
    public static final String PASSWORD = "password";
    public static final String ENCODED_PASSWORD = "encoded-password";
    public static final String ACCESS_TOKEN = "access-token";
    public static final String REFRESH_TOKEN = "refresh-token";
    public static final String OLD_TOKEN = "old_token";
    public static final String NEW_TOKEN = "new_token";

    public static RegisterRequest registerRequest() {
        RegisterRequest r = new RegisterRequest();
        r.setUsername(USERNAME);
        r.setEmail(EMAIL);
        r.setPassword(PASSWORD);

        return r;
    }

    public static LoginRequest loginRequest() {
        LoginRequest r = new LoginRequest();
        r.setIdentifier(USERNAME);
        r.setPassword(PASSWORD);

        return r;
    }

    public static RefreshTokenRequest refreshTokenRequest(String token) {
        RefreshTokenRequest r = new RefreshTokenRequest();
        r.setRefreshToken(token);

        return r;
    }
}
