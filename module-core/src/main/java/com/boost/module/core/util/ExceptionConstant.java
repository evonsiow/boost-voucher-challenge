package com.boost.module.core.util;

import lombok.Getter;

public class ExceptionConstant {
    @Getter
    public enum Exception {
        COMMON_EXCEPTION("ERR1001", "An error thrown while processing request.", "An error thrown while processing request."),
        DUPLICATE_EMAIL_EXCEPTION("ERR1002", "Email has already exists.", "Email has already exists");

        private String code;

        private String exceptionMessage;

        private String errorMessage;

        Exception(String code, String exceptionMessage, String errorMessage) {
            this.code = code;
            this.exceptionMessage = exceptionMessage;
            this.errorMessage = errorMessage;
        }
    }
}
