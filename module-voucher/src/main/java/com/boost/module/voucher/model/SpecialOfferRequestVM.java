package com.boost.module.voucher.model;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SpecialOfferRequestVM {
    @NotBlank(message = "Name is required")
    private String name;

    @NotNull(message = "Discount percentage is required")
    @Min(value = 1, message = "Discount percentage must be at least 1")
    @Max(value = 100, message = "Discount percentage cannot exceed 100")
    private Integer discountPercentage;

    public SpecialOfferRequestVM() {
    }

    public SpecialOfferRequestVM(String name, Integer discountPercentage) {
        this.name = name;
        this.discountPercentage = discountPercentage;
    }
}
