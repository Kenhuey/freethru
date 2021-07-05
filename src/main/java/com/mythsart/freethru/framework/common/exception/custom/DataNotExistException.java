package com.mythsart.freethru.framework.common.exception.custom;

public class DataNotExistException extends Exception {

    public DataNotExistException(final String message) {
        super(message);
    }

    public DataNotExistException() {
        super("Data not exist.");
    }

}
