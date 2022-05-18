package com.epam.esm.gifts.security;

import org.springframework.data.domain.AuditorAware;

import java.util.Optional;

public class SecurityAuditAware implements AuditorAware<String> {

    private final JwtUser user = new JwtUser();


    @Override
    public Optional<String> getCurrentAuditor() {
        System.out.println(user.getUsername());
        return Optional.ofNullable(user.getUsername());
    }
}
