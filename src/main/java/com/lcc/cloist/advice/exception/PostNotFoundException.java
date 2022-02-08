package com.lcc.cloist.advice.exception;

public class PostNotFoundException extends RuntimeException {
    public PostNotFoundException(String message, Throwable t) {
        super(message, t);
    }

    public PostNotFoundException(String message) {
        super(message);
    }

    public PostNotFoundException() {
        super();
    }
}
