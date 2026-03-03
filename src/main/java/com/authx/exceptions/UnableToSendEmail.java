package com.authx.exceptions;

public class UnableToSendEmail extends RuntimeException {
    public UnableToSendEmail(String message) {
        super(message);
    }
}
