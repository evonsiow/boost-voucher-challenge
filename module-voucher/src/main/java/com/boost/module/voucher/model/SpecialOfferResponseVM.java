package com.boost.module.voucher.model;

import com.boost.module.voucher.dto.SpecialOfferDTO;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
public class SpecialOfferResponseVM {
    private UUID id;
    private String name;
    private Integer discountPercentage;

    public SpecialOfferResponseVM() {
    }

    public SpecialOfferResponseVM(UUID id, String name, Integer discountPercentage) {
        this.id = id;
        this.name = name;
        this.discountPercentage = discountPercentage;
    }

    public static SpecialOfferResponseVM map(SpecialOfferDTO specialOfferDTO) {
        SpecialOfferResponseVM specialOfferResponseVM = new SpecialOfferResponseVM();

        specialOfferResponseVM.setId(specialOfferDTO.getId());
        specialOfferResponseVM.setName(specialOfferDTO.getName());
        specialOfferResponseVM.setDiscountPercentage(specialOfferDTO.getDiscountPercentage());

        return specialOfferResponseVM;
    }
}
