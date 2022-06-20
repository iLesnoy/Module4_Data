package com.epam.esm.gifts.security;

import com.epam.esm.gifts.model.User;
import org.springframework.security.core.userdetails.UserDetails;

public class SecurityUserDetailsBuilder {
    private static final boolean ACTIVE = true;

    private SecurityUserDetailsBuilder() {
    }

    public static UserDetails create(User user) {
        return new JwtUser(
                user.getId(),
                user.getName(),
                user.getPassword(),
                user.getRole().getAuthorities(),
                ACTIVE
        );
    }
}
