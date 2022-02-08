package com.lcc.cloist.advice.exception;

public class SignInFailedException extends RuntimeException {
    public SignInFailedException(String message, Throwable t) {
        super(message, t);
    }

    public SignInFailedException(String message) {
        super(message);
    }

    public SignInFailedException() {
        super();
    }
}
