package com.salon.common.exception;

/**
 * ErrorResponse: standard error response body returned to client
 */
public class ErrorResponse {
    public int status;
    public String message;
    public long timestamp;

    public ErrorResponse(int status, String message) {
        this.status = status;
        this.message = message;
        this.timestamp = System.currentTimeMillis();
    }
}
