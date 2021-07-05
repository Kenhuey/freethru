package com.mythsart.freethru.framework.common.response;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

public class ResponseBuilder {

    private ResponseBuilder() {
    }

    /**
     * Response code: ANY
     * Status: ANY
     **/

    public static <T> ResponseEntity<ResponseResult<T>> makeResponse(final T data, final HttpStatus code, final String message) {
        return new ResponseEntity<>(
                new ResponseResult<T>()
                        .setData(data)
                        .setCode(code)
                        .setMessage(message),
                code
        );
    }

    public static <T> ResponseEntity<ResponseResult<T>> makeResponse(final HttpStatus code, final String message) {
        return new ResponseEntity<>(
                new ResponseResult<T>()
                        .setCode(code)
                        .setMessage(message),
                code
        );
    }

    /**
     * Response code: 200
     * Status: OK
     **/

    public final static String MESSAGE_OK = "Success request.";

    public static <T> ResponseEntity<ResponseResult<T>> makeOkResponse(final T data) {
        return new ResponseEntity<>(
                new ResponseResult<T>()
                        .setData(data)
                        .setCode(HttpStatus.OK)
                        .setMessage(MESSAGE_OK),
                HttpStatus.OK
        );
    }

    public static <T> ResponseEntity<ResponseResult<T>> makeOkResponse() {
        return new ResponseEntity<>(
                new ResponseResult<T>()
                        .setCode(HttpStatus.OK)
                        .setMessage(MESSAGE_OK),
                HttpStatus.OK
        );
    }

    /**
     * Response code: 400
     * Status: BAD REQUEST
     **/

    public final static String MESSAGE_BAD_REQUEST = "Fail request.";

    public static <T> ResponseEntity<ResponseResult<T>> makeBadRequestResponse(final T data) {
        return new ResponseEntity<>(
                new ResponseResult<T>()
                        .setData(data)
                        .setCode(HttpStatus.BAD_REQUEST)
                        .setMessage(MESSAGE_BAD_REQUEST),
                HttpStatus.BAD_REQUEST
        );
    }

    public static <T> ResponseEntity<ResponseResult<T>> makeBadRequestResponse() {
        return new ResponseEntity<>(
                new ResponseResult<T>()
                        .setCode(HttpStatus.BAD_REQUEST)
                        .setMessage(MESSAGE_BAD_REQUEST),
                HttpStatus.BAD_REQUEST
        );
    }

    /**
     * Response code: 500
     * Status: INTERNAL SERVER ERROR
     **/

    public final static String MESSAGE_INTERNAL_SERVER_ERROR = "Server error.";

    public static <T> ResponseEntity<ResponseResult<T>> makeInternalServerErrorResponse(final T data) {
        return new ResponseEntity<>(
                new ResponseResult<T>()
                        .setData(data)
                        .setCode(HttpStatus.INTERNAL_SERVER_ERROR)
                        .setMessage(MESSAGE_INTERNAL_SERVER_ERROR),
                HttpStatus.INTERNAL_SERVER_ERROR
        );
    }

    public static <T> ResponseEntity<ResponseResult<T>> makeInternalServerErrorResponse() {
        return new ResponseEntity<>(
                new ResponseResult<T>()
                        .setCode(HttpStatus.INTERNAL_SERVER_ERROR)
                        .setMessage(MESSAGE_INTERNAL_SERVER_ERROR),
                HttpStatus.INTERNAL_SERVER_ERROR
        );
    }

    /**
     * Response code: 403
     * Status: UNAUTHORIZED
     **/

    public final static String MESSAGE_UNAUTHORIZED = "Authorize not available.";

    public static <T> ResponseEntity<ResponseResult<T>> makeUnAuthorizedResponse(final T data) {
        return new ResponseEntity<>(
                new ResponseResult<T>()
                        .setData(data)
                        .setCode(HttpStatus.UNAUTHORIZED)
                        .setMessage(MESSAGE_UNAUTHORIZED),
                HttpStatus.UNAUTHORIZED
        );
    }

    public static <T> ResponseEntity<ResponseResult<T>> makeUnAuthorizedResponse() {
        return new ResponseEntity<>(
                new ResponseResult<T>()
                        .setCode(HttpStatus.UNAUTHORIZED)
                        .setMessage(MESSAGE_UNAUTHORIZED),
                HttpStatus.UNAUTHORIZED
        );
    }

    /**
     * Response code: 404
     * Status: UNAUTHORIZED
     **/

    public final static String MESSAGE_NOT_FOUND = "Not found.";

    public static <T> ResponseEntity<ResponseResult<T>> makeNotFoundResponse(final T data) {
        return new ResponseEntity<>(
                new ResponseResult<T>()
                        .setData(data)
                        .setCode(HttpStatus.NOT_FOUND)
                        .setMessage(MESSAGE_NOT_FOUND),
                HttpStatus.NOT_FOUND
        );
    }

    public static <T> ResponseEntity<ResponseResult<T>> makeNotFoundResponse() {
        return new ResponseEntity<>(
                new ResponseResult<T>()
                        .setCode(HttpStatus.NOT_FOUND)
                        .setMessage(MESSAGE_NOT_FOUND),
                HttpStatus.NOT_FOUND
        );
    }

}
