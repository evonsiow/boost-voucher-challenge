package com.boost.module.core.util;

import lombok.Getter;

public class ExceptionConstant {
    @Getter
    public enum Exception {
        COMMON_EXCEPTION("ERR1001", "An error thrown while processing request.", "An error thrown while processing request."),
        DUPLICATE_EMAIL_EXCEPTION("ERR1002", "Email exists.", "Email exists."),
        INVALID_RECIPIENT_EXCEPTION("ERR1003", "Recipient not found.", "Recipient not found."),
        DUPLICATE_SPECIAL_OFFER_EXCEPTION("ERR1004", "Special offer name exists.", "Special offer name exists."),
        INVALID_SPECIAL_OFFER_EXCEPTION("ERR1005", "Special offer not found.", "Special offer not found."),
        ERROR_USER_MS_EXCEPTION("ERR1006", "User MS is down.", "User MS is down.");

        private final String code;

        private final String exceptionMessage;

        private final String errorMessage;

        Exception(String code, String exceptionMessage, String errorMessage) {
            this.code = code;
            this.exceptionMessage = exceptionMessage;
            this.errorMessage = errorMessage;
        }
    }
}
