package com.lcc.cloist.advice.exception;

public class VoteNotFoundException extends RuntimeException {
    public VoteNotFoundException(String message, Throwable t) {
        super(message, t);
    }

    public VoteNotFoundException(String message) {
        super(message);
    }

    public VoteNotFoundException() {
        super();
    }
}
