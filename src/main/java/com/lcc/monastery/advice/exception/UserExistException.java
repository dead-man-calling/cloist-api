package com.lcc.monastery.advice.exception;

public class UserExistException extends RuntimeException {
    public UserExistException(String message, Throwable t) {
        super(message, t);
    }

    public UserExistException(String message) {
        super(message);
    }

    public UserExistException() {
        super();
    }
}
