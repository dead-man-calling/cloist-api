package com.lcc.cloist.advice.exception;

public class VoteExistException extends RuntimeException {
    public VoteExistException(String message, Throwable t) {
        super(message, t);
    }

    public VoteExistException(String message) {
        super(message);
    }

    public VoteExistException() {
        super();
    }
}