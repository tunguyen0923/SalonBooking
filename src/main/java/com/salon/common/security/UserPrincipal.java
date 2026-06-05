package com.salon.common.security;

import java.io.Serializable;

/**
 * UserPrincipal: typed principal extracted from JWT claims.
 * Provides easy access to userId, salonId, and role without Claims casting.
 */
public class UserPrincipal implements Serializable {

    private final Long userId;
    private final Long salonId;
    private final String role;

    public UserPrincipal(Long userId, Long salonId, String role) {
        this.userId = userId;
        this.salonId = salonId;
        this.role = role;
    }

    public Long getUserId() {
        return userId;
    }

    public Long getSalonId() {
        return salonId;
    }

    public String getRole() {
        return role;
    }

    @Override
    public String toString() {
        return "UserPrincipal{" +
                "userId=" + userId +
                ", salonId=" + salonId +
                ", role='" + role + '\'' +
                '}';
    }
}
