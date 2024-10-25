package com.burntbean.burntbean.exception;

public class CustomServiceException extends RuntimeException {
    public CustomServiceException(String message, Throwable cause) {
        super(message, cause);
    }
}