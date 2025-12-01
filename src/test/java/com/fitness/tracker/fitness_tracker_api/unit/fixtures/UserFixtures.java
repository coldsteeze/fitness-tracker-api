package com.fitness.tracker.fitness_tracker_api.unit.fixtures;

import com.fitness.tracker.fitness_tracker_api.entity.User;
import lombok.experimental.UtilityClass;

@UtilityClass
public class UserFixtures {

    public static User user() {
        User user = new User();
        user.setId(1L);
        user.setUsername(AuthRequestFixtures.USERNAME);
        user.setEmail(AuthRequestFixtures.EMAIL);

        return user;
    }
}
