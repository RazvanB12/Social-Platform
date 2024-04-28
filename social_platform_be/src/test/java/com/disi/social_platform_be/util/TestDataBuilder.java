package com.disi.social_platform_be.util;

import com.disi.social_platform_be.model.Detail;
import com.disi.social_platform_be.model.User;
import com.disi.social_platform_be.model.enums.Role;

import java.util.UUID;

public class TestDataBuilder {

    public static User createUser(UUID userId) {
        User user = new User();
        user.setId(userId);
        user.setActive(true);
        user.setRole(Role.CLIENT);
        user.setDetail(new Detail());
        return user;
    }
}
