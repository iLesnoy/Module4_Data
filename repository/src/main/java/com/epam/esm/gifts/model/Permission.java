package com.epam.esm.gifts.model;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public enum Permission {

    TAGS_CREATE("tags:create"),
    TAGS_DELETE("tags:delete"),

    CERTIFICATES_CREATE("certificates:create"),
    CERTIFICATES_UPDATE("certificates:update"),
    CERTIFICATES_DELETE("certificates:delete"),

    USERS_CREATE("users:create"),
    USERS_READ("users:read"),
    USERS_DELETE("users:delete"),

    ORDERS_CREATE("orders:create"),
    ORDERS_READ("orders:read"),

    STATISTICS_READ("statistics:read"),

    AUTH_SIGN_UP("auth:sign_up");

    private final String permission;
}
