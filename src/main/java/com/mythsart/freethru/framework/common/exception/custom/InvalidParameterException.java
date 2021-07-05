package com.mythsart.freethru.framework.common.exception.custom;

public class InvalidParameterException extends Exception{

    public InvalidParameterException(final String message) {
        super(message);
    }

    public InvalidParameterException() {
        super("Invalid parameter.");
    }

}
