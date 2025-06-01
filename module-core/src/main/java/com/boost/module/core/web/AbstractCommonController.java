package com.boost.module.core.web;

import com.boost.module.core.model.ApiResponse;
import com.boost.module.core.util.ExceptionConstant;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;

public abstract class AbstractCommonController {
    protected <T> ResponseEntity<ApiResponse<T>> successResponse(T data) {
        return ResponseEntity.ok(
                new ApiResponse<>(HttpStatus.OK.value(), true, null, "Success", data)
        );
    }

    protected <T> ResponseEntity<ApiResponse<T>> successResponse(String message, T data) {
        return ResponseEntity.ok(
                new ApiResponse<>(HttpStatus.OK.value(), true, null, message, data)
        );
    }

    protected ResponseEntity<ApiResponse<Object>> failedResponse(String message) {
        return ResponseEntity.badRequest().body(
                new ApiResponse<>(HttpStatus.BAD_REQUEST.value(), false, null, message, null)
        );
    }

    protected ResponseEntity<ApiResponse<Object>> failedResponse(String errorCode, String message, HttpStatus status) {
        return ResponseEntity.status(status).body(
                new ApiResponse<>(status.value(), false, errorCode, message, null)
        );
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ApiResponse<Object>> handleIllegalArgumentException(IllegalArgumentException illegalArgumentException) {
        return failedResponse(ExceptionConstant.Exception.COMMON_EXCEPTION.getCode(),
                illegalArgumentException.getMessage(), HttpStatus.BAD_REQUEST);
    }
}
