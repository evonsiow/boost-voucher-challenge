package com.boost.module.voucher.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ValidateVoucherResponseVM {
    private boolean valid;
    private Integer discountPercentage;
    private String message;
    private String offerName;

    public ValidateVoucherResponseVM() {
    }

    public ValidateVoucherResponseVM(boolean valid, Integer discountPercentage, String message, String offerName) {
        this.valid = valid;
        this.discountPercentage = discountPercentage;
        this.message = message;
        this.offerName = offerName;
    }
}
