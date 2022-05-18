package com.epam.esm.gifts.security;

import org.springframework.data.domain.AuditorAware;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Optional;

public class SecurityAuditAware implements AuditorAware<String> {


    @Override
    public Optional<String> getCurrentAuditor() {
        Authentication auth  = SecurityContextHolder.getContext().getAuthentication();
        return Optional.ofNullable(auth.getName());
    }
}
