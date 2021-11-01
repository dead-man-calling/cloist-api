package com.lcc.monastery.advice.exception;

public class InvalidFormException extends RuntimeException {
    public InvalidFormException(String message, Throwable t) {
        super(message, t);
    }

    public InvalidFormException(String message) {
        super(message);
    }

    public InvalidFormException() {
        super();
    }
}
