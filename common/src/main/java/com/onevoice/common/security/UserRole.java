package com.onevoice.common.security;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public enum UserRole {

    USER("ROLE_USER"),
    ADMIN("ROLE_ADMIN");

    private final String roleName;

    public String roleName() {
        return roleName;
    }

    public static UserRole from(String value) {
        for (UserRole role : values()) {
            if (role.roleName.equals(value) || role.name().equals(value)) {
                return role;
            }
        }
        throw new IllegalArgumentException("Unknown role: " + value);
    }

}
