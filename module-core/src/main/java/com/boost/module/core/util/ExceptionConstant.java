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
        INVALID_RECIPIENT_MS_EXCEPTION("ERR1006", "Recipient MS is down.", "Recipient MS is down."),
        INVALID_VOUCHER_CODE_EXCEPTION("ERR1007", "Voucher code not found.", "Voucher code not found."),
        INVALID_VOUCHER_RECIPIENT_EXCEPTION("ERR1008", "This voucher code is not assigned to the provided email.", "This voucher code is not assigned to the provided email."),
        USED_VOUCHER_EXCEPTION("ERR1009", "This voucher has already been used.", "This voucher has already been used."),
        EXPIRED_VOUCHER_EXCEPTION("ERR1010", "This voucher has already been expired.", "This voucher has already been expired.");

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
