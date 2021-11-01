package com.lcc.monastery.advice.exception;

public class CommunicationErrorException extends RuntimeException {
    public CommunicationErrorException(String message, Throwable t) {
        super(message, t);
    }

    public CommunicationErrorException(String message) {
        super(message);
    }

    public CommunicationErrorException() {
        super();
    }
}
