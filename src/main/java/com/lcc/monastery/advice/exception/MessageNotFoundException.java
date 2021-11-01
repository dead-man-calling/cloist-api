package com.lcc.monastery.advice.exception;

public class MessageNotFoundException extends RuntimeException {
    public MessageNotFoundException(String message, Throwable t) {
        super(message, t);
    }

    public MessageNotFoundException(String message) {
        super(message);
    }

    public MessageNotFoundException() {
        super();
    }
}
