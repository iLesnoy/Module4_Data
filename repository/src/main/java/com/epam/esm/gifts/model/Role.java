package com.epam.esm.gifts.model;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.Set;
import java.util.stream.Collectors;

import static com.epam.esm.gifts.model.Permission.*;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
public enum Role {

    ADMIN(Set.of(TAGS_CREATE, TAGS_DELETE, CERTIFICATES_CREATE, CERTIFICATES_UPDATE, CERTIFICATES_DELETE, ORDERS_CREATE
            , ORDERS_READ, USERS_CREATE, USERS_READ, USERS_DELETE, STATISTICS_READ, AUTH_SIGN_UP)),
    USER(Set.of(ORDERS_CREATE));

    private final Set<Permission> permissionSet;

    public Set<SimpleGrantedAuthority> getAuthorities() {
        return permissionSet.stream()
                .map(permission -> new SimpleGrantedAuthority(permission.getPermission()))
                .collect(Collectors.toSet());
    }
}
