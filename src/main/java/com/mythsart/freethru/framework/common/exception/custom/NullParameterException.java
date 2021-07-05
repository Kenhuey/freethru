package com.mythsart.freethru.framework.common.exception.custom;

public class NullParameterException extends Exception {

    public NullParameterException(final String message) {
        super(message);
    }

    public NullParameterException() {
        super("Null parameter.");
    }

}
