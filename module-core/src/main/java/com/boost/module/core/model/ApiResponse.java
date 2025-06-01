package com.boost.module.core.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;

@Getter
@Setter
public class ApiResponse<T> {
    private Instant timestamp;
    private int status;

    @JsonProperty("isSuccess")
    private boolean success;

    private String errorCode;

    private String message;

    private T data;

    public ApiResponse() {
        this.timestamp = Instant.now();
    }

    public ApiResponse(int status, boolean success, String message, T data) {
        this.timestamp = Instant.now();
        this.status = status;
        this.success = success;
        this.message = message;
        this.data = data;
    }

    public ApiResponse(int status, boolean success, String errorCode, String message, T data) {
        this.timestamp = Instant.now();
        this.status = status;
        this.success = success;
        this.errorCode = errorCode;
        this.message = message;
        this.data = data;
    }
}
