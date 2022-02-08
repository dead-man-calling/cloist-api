package com.lcc.cloist.advice.exception;

public class AuthenticationEntryPointException extends RuntimeException {
    public AuthenticationEntryPointException(String message, Throwable t) {
        super(message, t);
    }

    public AuthenticationEntryPointException(String message) {
        super(message);
    }

    public AuthenticationEntryPointException() {
        super();
    }
}
