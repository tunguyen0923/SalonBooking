package com.salon.common.security;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

/**
 * SecurityUtil: helper to extract user context from SecurityContextHolder.
 * Makes role checks and tenant-scoped access easier throughout the app.
 */
@Component
public class SecurityUtil {

    /**
     * Get the current UserPrincipal from the security context.
     * @return UserPrincipal if authenticated, null otherwise
     */
    public static UserPrincipal getCurrentUser() {
        SecurityContext context = SecurityContextHolder.getContext();
        if (context == null) return null;
        Authentication auth = context.getAuthentication();
        if (auth == null) return null;
        Object principal = auth.getPrincipal();
        if (principal instanceof UserPrincipal) {
            return (UserPrincipal) principal;
        }
        return null;
    }

    /**
     * Get current user's salonId.
     * @return salonId or null if not authenticated
     */
    public static Long getCurrentSalonId() {
        UserPrincipal user = getCurrentUser();
        return user != null ? user.getSalonId() : null;
    }

    /**
     * Get current user's userId.
     * @return userId or null if not authenticated
     */
    public static Long getCurrentUserId() {
        UserPrincipal user = getCurrentUser();
        return user != null ? user.getUserId() : null;
    }

    /**
     * Get current user's role.
     * @return role or null if not authenticated
     */
    public static String getCurrentRole() {
        UserPrincipal user = getCurrentUser();
        return user != null ? user.getRole() : null;
    }
}
