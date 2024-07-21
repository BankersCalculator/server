package com.bankersCalculator.server.common.api;

import lombok.Getter;
import org.springframework.http.HttpStatus;

import static org.springframework.http.HttpStatus.*;

/***
 * ResponseEntity를 대체할 Api 공통 스펙
 * @param <T> 제네릭. 반환할 데이터 타입
 */
@Getter
public class ApiResponse<T> {

    private final int code;
    private final HttpStatus status;
    private final String message;
    private final T data;

    public ApiResponse(HttpStatus status, String message, T data) {
        this.code = status.value();
        this.status = status;
        this.message = message;
        this.data = data;
    }

    public static <T> ApiResponse<T> of(HttpStatus status, String message, T data) {
        return new ApiResponse<>(status, message, data);
    }

    public static <T> ApiResponse<T> ok(T data) {
        return of(OK, "OK", data);
    }

    public static <T> ApiResponse<T> success(T data) {
        return new ApiResponse<>(OK, "SUCCESS", data);
    }
}
