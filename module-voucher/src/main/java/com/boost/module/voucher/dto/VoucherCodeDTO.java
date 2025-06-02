package com.boost.module.voucher.dto;

import com.boost.module.voucher.db.entity.VoucherCodeVO;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
public class VoucherCodeDTO {
    private UUID id;
    private String code;
    private UUID recipientId;
    private UUID specialOfferId;
    private LocalDateTime expirationDate;
    private LocalDateTime usedDate;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public VoucherCodeDTO() {
    }

    public Boolean isUsed() {
        return usedDate != null;
    }

    public Boolean isExpired() {
        return expirationDate != null && LocalDateTime.now().isAfter(expirationDate);
    }

    public Boolean isValid() {
        return !isUsed() && !isExpired();
    }

    public static VoucherCodeDTO map(VoucherCodeVO voucherCodeVO) {
        VoucherCodeDTO voucherCodeDTO = new VoucherCodeDTO();

        voucherCodeDTO.setId(voucherCodeVO.getId());
        voucherCodeDTO.setCode(voucherCodeVO.getCode());
        voucherCodeDTO.setRecipientId(voucherCodeVO.getRecipientId());
        voucherCodeDTO.setSpecialOfferId(voucherCodeVO.getSpecialOfferId());
        voucherCodeDTO.setExpirationDate(voucherCodeVO.getExpirationDate());
        voucherCodeDTO.setUsedDate(voucherCodeVO.getUsedDate());
        voucherCodeDTO.setCreatedAt(voucherCodeVO.getCreatedAt());
        voucherCodeDTO.setUpdatedAt(voucherCodeVO.getUpdatedAt());

        return voucherCodeDTO;
    }
}
