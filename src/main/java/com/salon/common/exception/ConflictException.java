package com.salon.common.exception;

/**
 * ConflictException: 409 conflict (e.g., duplicate email, double booking)
 */
public class ConflictException extends RuntimeException {
    public ConflictException(String message) {
        super(message);
    }

    public ConflictException(String message, Throwable cause) {
        super(message, cause);
    }
}
