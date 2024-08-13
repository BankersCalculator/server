package com.bankersCalculator.server.common.exception;

import com.bankersCalculator.server.common.api.ApiResponse;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.validation.BindException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.NoSuchElementException;

@RestControllerAdvice
public class ExceptionAdvisor {

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(BindException.class)
    public ApiResponse<Object> bindException(BindException e) {
        return ApiResponse.of(
            HttpStatus.BAD_REQUEST,
            e.getBindingResult().getAllErrors().get(0).getDefaultMessage(),
            null
        );
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(IllegalArgumentException.class)
    public ApiResponse<Object> illegalArgumentException(IllegalArgumentException e) {
        return ApiResponse.of(
            HttpStatus.BAD_REQUEST,
            e.getMessage(),
            null
        );
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(NoSuchElementException.class)
    public ApiResponse<Object> noSuchElementException(NoSuchElementException e) {
        return ApiResponse.of(
            HttpStatus.BAD_REQUEST,
            e.getMessage(),
            null
        );
    }

    @ExceptionHandler(AccessDeniedException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public ApiResponse<Object> accessDeniedException(AccessDeniedException e) {
        return ApiResponse.of(
            HttpStatus.UNAUTHORIZED,
            e.getMessage(),
            null
        );
    }
}
