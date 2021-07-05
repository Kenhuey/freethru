package com.mythsart.freethru.framework.common.exception.custom;

public class NullTokenException extends Exception {

    public NullTokenException(final String message) {
        super(message);
    }

    public NullTokenException() {
        super("Null token.");
    }

}
