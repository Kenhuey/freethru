package com.mythsart.freethru.framework.common.response;

import lombok.Data;
import org.springframework.http.HttpStatus;

@Data
public class ResponseResult<T> {

    private HttpStatus code;

    private String message;

    private T data;

    public ResponseResult<T> setCode(final HttpStatus code) {
        this.code = code;
        return this;
    }

    public int getCode() {
        return this.code.value();
    }

    public ResponseResult<T> setMessage(final String message) {
        this.message = message;
        return this;
    }

    public String getMessage() {
        return this.message;
    }

    public ResponseResult<T> setData(final T data) {
        this.data = data;
        return this;
    }

    public T getData() {
        return this.data;
    }

}
