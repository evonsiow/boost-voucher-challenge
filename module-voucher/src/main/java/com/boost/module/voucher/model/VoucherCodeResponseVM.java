package com.boost.module.voucher.model;

import com.boost.module.voucher.dto.VoucherCodeDTO;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class VoucherCodeResponseVM {
    private UUID id;
    private String code;
    private String recipientEmail;
    private String recipientName;
    private String offerName;
    private Integer discountPercentage;
    private LocalDateTime expirationDate;
    private LocalDateTime usedDate;
    private Boolean isValid;
    private LocalDateTime createdAt;

    public VoucherCodeResponseVM() {
    }

    public static VoucherCodeResponseVM map(VoucherCodeDTO voucherCodeDTO) {
        VoucherCodeResponseVM voucherCodeResponseVM = new VoucherCodeResponseVM();

        voucherCodeResponseVM.setId(voucherCodeDTO.getId());
        voucherCodeResponseVM.setCode(voucherCodeDTO.getCode());
        voucherCodeResponseVM.setExpirationDate(voucherCodeDTO.getExpirationDate());
        voucherCodeResponseVM.setUsedDate(voucherCodeDTO.getUsedDate());
        voucherCodeResponseVM.setIsValid(voucherCodeDTO.isValid());
        voucherCodeResponseVM.setCreatedAt(voucherCodeDTO.getCreatedAt());

        return voucherCodeResponseVM;
    }
}
