package com.mythsart.freethru.framework.common.exception.custom;

public class DataExistException extends Exception {

    public DataExistException(final String message) {
        super(message);
    }

    public DataExistException() {
        super("Data exist.");
    }

}
