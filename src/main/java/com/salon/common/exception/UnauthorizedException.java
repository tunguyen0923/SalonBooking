package com.salon.common.exception;

/**
 * UnauthorizedException: 401 unauthorized or 403 forbidden
 */
public class UnauthorizedException extends RuntimeException {
    public UnauthorizedException(String message) {
        super(message);
    }

    public UnauthorizedException(String message, Throwable cause) {
        super(message, cause);
    }
}
