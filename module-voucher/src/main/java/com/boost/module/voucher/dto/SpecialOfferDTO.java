package com.boost.module.voucher.dto;

import com.boost.module.voucher.db.entity.SpecialOfferVO;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
public class SpecialOfferDTO {
    private UUID id;

    @NotBlank(message = "Name is required")
    private String name;

    @NotNull(message = "Discount percentage is required")
    @Min(value = 1, message = "Discount percentage must be at least 1")
    @Max(value = 100, message = "Discount percentage cannot exceed 100")
    private Integer discountPercentage;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    public SpecialOfferDTO() {
    }

    public SpecialOfferDTO(UUID id, String name, Integer discountPercentage, LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.id = id;
        this.name = name;
        this.discountPercentage = discountPercentage;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public static SpecialOfferDTO map(SpecialOfferVO specialOfferVO) {
        SpecialOfferDTO specialOfferDTO = new SpecialOfferDTO();

        specialOfferDTO.setId(specialOfferVO.getId());
        specialOfferDTO.setName(specialOfferVO.getName());
        specialOfferDTO.setDiscountPercentage(specialOfferVO.getDiscountPercentage());
        specialOfferDTO.setCreatedAt(specialOfferVO.getCreatedAt());
        specialOfferDTO.setUpdatedAt(specialOfferVO.getUpdatedAt());

        return specialOfferDTO;
    }
}
