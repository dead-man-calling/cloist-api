package com.lcc.monastery.advice.exception;

public class GatheringNotFoundException extends RuntimeException {
    public GatheringNotFoundException(String message, Throwable t) {
        super(message, t);
    }

    public GatheringNotFoundException(String message) {
        super(message);
    }

    public GatheringNotFoundException() {
        super();
    }
}
