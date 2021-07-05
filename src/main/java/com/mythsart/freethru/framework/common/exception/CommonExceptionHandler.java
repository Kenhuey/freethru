package com.mythsart.freethru.framework.common.exception;

import com.auth0.jwt.exceptions.JWTDecodeException;
import com.auth0.jwt.exceptions.SignatureVerificationException;
import com.auth0.jwt.exceptions.TokenExpiredException;
import com.mythsart.freethru.framework.common.exception.custom.*;
import com.mythsart.freethru.framework.common.response.ResponseBuilder;
import com.mythsart.freethru.framework.common.response.ResponseResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.InvalidDataAccessResourceUsageException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.io.IOException;
import java.sql.SQLIntegrityConstraintViolationException;

@RestControllerAdvice
public class CommonExceptionHandler {

    private final Logger logger = LoggerFactory.getLogger(CommonExceptionHandler.class);

    @ExceptionHandler(value = SignatureVerificationException.class)
    public ResponseEntity<ResponseResult<Object>> SignatureVerificationException(final AccessDeniedException exception) {
        return ResponseBuilder.makeResponse(null, HttpStatus.UNAUTHORIZED, "Token signature not match.");
    }


    @ExceptionHandler(value = AccessDeniedException.class)
    public ResponseEntity<ResponseResult<Object>> AccessDeniedException(final AccessDeniedException exception) {
        return ResponseBuilder.makeResponse(null, HttpStatus.FORBIDDEN, "Access forbidden.");
    }

    @ExceptionHandler(value = IOException.class)
    public ResponseEntity<ResponseResult<Object>> IOException(final IOException exception) {
        return ResponseBuilder.makeResponse(null, HttpStatus.INTERNAL_SERVER_ERROR, "File stream error.");
    }

    /**
     * Any exception.
     **/
    @ExceptionHandler(value = Exception.class)
    public ResponseEntity<ResponseResult<Object>> Exception(final Exception exception) {
        exception.printStackTrace();
        return ResponseBuilder.makeResponse(null, HttpStatus.INTERNAL_SERVER_ERROR, "Unknown error(s).");
    }

    @ExceptionHandler(value = UserLoginFailException.class)
    public ResponseEntity<ResponseResult<Object>> UserLoginFailException() {
        return ResponseBuilder.makeUnAuthorizedResponse();
    }

    @ExceptionHandler(value = NullParameterException.class)
    public ResponseEntity<ResponseResult<Object>> NullParameterException(final Exception exception) {
        exception.printStackTrace();
        return ResponseBuilder.makeInternalServerErrorResponse("Null param.");
    }

    @ExceptionHandler(value = NullTokenException.class)
    public ResponseEntity<ResponseResult<Object>> NullTokenException(final Exception exception) {
        return ResponseBuilder.makeResponse(null, HttpStatus.UNAUTHORIZED, exception.getMessage());
    }

    @ExceptionHandler(value = UserNotExistException.class)
    public ResponseEntity<ResponseResult<Object>> UserNotExistException(final Exception exception) {
        return ResponseBuilder.makeResponse(null, HttpStatus.UNAUTHORIZED, exception.getMessage());
    }

    @ExceptionHandler(value = TokenExpiredException.class)
    public ResponseEntity<ResponseResult<Object>> TokenExpiredException(final Exception exception) {
        return ResponseBuilder.makeResponse(null, HttpStatus.UNAUTHORIZED, exception.getMessage());
    }

    @ExceptionHandler(value = MethodArgumentTypeMismatchException.class)
    public ResponseEntity<ResponseResult<Object>> MethodArgumentTypeMismatchException() {
        return ResponseBuilder.makeResponse(null, HttpStatus.BAD_REQUEST, "Request param(s) not match.");
    }

    @ExceptionHandler(value = JWTDecodeException.class)
    public ResponseEntity<ResponseResult<Object>> JWTDecodeException() {
        return ResponseBuilder.makeResponse(null, HttpStatus.UNAUTHORIZED, "Not a valid token.");
    }

    @ExceptionHandler(value = InvalidParameterException.class)
    public ResponseEntity<ResponseResult<Object>> InvalidParameterException(final Exception exception) {
        return ResponseBuilder.makeResponse(null, HttpStatus.BAD_REQUEST, exception.getMessage());
    }

    @ExceptionHandler(value = InvalidDataAccessResourceUsageException.class)
    public ResponseEntity<ResponseResult<Object>> InvalidDataAccessResourceUsageException(final Exception exception) {
        exception.printStackTrace();
        return ResponseBuilder.makeResponse(null, HttpStatus.INTERNAL_SERVER_ERROR, "Database(s) error(s).");
    }

    @ExceptionHandler(value = DataExistException.class)
    public ResponseEntity<ResponseResult<Object>> DataExistException(final Exception exception) {
        return ResponseBuilder.makeResponse(null, HttpStatus.BAD_REQUEST, exception.getMessage());
    }

    @ExceptionHandler(value = SQLIntegrityConstraintViolationException.class)
    public ResponseEntity<ResponseResult<Object>> SQLIntegrityConstraintViolationException(final Exception exception) {
        exception.printStackTrace();
        return ResponseBuilder.makeResponse(null, HttpStatus.BAD_REQUEST, "Data(s) not valid.");
    }

    @ExceptionHandler(value = DataIntegrityViolationException.class)
    public ResponseEntity<ResponseResult<Object>> DataIntegrityViolationException(final Exception exception) {
        return this.SQLIntegrityConstraintViolationException(exception);
    }

    @ExceptionHandler(value = DataNotExistException.class)
    public ResponseEntity<ResponseResult<Object>> DataNotExistException(final Exception exception) {
        return ResponseBuilder.makeResponse(null, HttpStatus.NOT_FOUND, exception.getMessage());
    }

    @ExceptionHandler(value = HttpRequestMethodNotSupportedException.class)
    public ResponseEntity<ResponseResult<Object>> HttpRequestMethodNotSupportedException(final Exception exception) {
        return ResponseBuilder.makeResponse(null, HttpStatus.METHOD_NOT_ALLOWED, exception.getMessage());
    }

}
