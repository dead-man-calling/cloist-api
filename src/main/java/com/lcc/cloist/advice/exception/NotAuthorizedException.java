package com.lcc.cloist.advice.exception;

public class NotAuthorizedException extends RuntimeException {
    public NotAuthorizedException(String message, Throwable t) {
        super(message, t);
    }

    public NotAuthorizedException(String message) {
        super(message);
    }

    public NotAuthorizedException() {
        super();
    }
}
