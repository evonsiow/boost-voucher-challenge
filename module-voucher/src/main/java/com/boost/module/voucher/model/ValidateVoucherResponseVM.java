package com.boost.module.voucher.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ValidateVoucherResponseVM {
    private Boolean isValid;
    private Integer discountPercentage;
    private String recipientEmail;

    public ValidateVoucherResponseVM() {
    }

    public ValidateVoucherResponseVM(Boolean isValid, Integer discountPercentage, String recipientEmail) {
        this.isValid = isValid;
        this.discountPercentage = discountPercentage;
        this.recipientEmail = recipientEmail;
    }
}
