package com.mythsart.freethru.framework.common.exception.custom;

public class UserNotExistException extends Exception {

    public UserNotExistException(final String message) {
        super(message);
    }

    public UserNotExistException() {
        super("User not exist.");
    }

}
