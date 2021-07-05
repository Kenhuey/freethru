package com.mythsart.freethru.framework.common.exception.custom;

public class UserLoginFailException extends Exception {

    public UserLoginFailException(final String message) {
        super(message);
    }

    public UserLoginFailException() {
        super("Login fail.");
    }

}
