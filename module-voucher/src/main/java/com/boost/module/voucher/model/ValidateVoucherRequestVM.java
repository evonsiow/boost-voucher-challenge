package com.boost.module.voucher.model;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ValidateVoucherRequestVM {
    @NotBlank(message = "Voucher code is required")
    private String code;

    @NotBlank(message = "Email is required")
    @Email(message = "Email should be valid")
    private String email;

    public ValidateVoucherRequestVM() {
    }

    public ValidateVoucherRequestVM(String code, String email) {
        this.code = code;
        this.email = email;
    }
}
