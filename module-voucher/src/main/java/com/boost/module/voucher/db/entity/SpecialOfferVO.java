package com.boost.module.voucher.db.entity;

import com.boost.module.voucher.dto.SpecialOfferDTO;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@Entity
@Table(name = "special_offers")
public class SpecialOfferVO {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id", nullable = false)
    private UUID id;
    
    @NotBlank(message = "Name is required")
    @Column(name = "name", nullable = false)
    private String name;
    
    @NotNull(message = "Discount percentage is required")
    @Min(value = 1, message = "Discount percentage must be at least 1")
    @Max(value = 100, message = "Discount percentage cannot exceed 100")
    @Column(name = "discount_percentage", nullable = false)
    private Integer discountPercentage;
    
    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;
    
    @UpdateTimestamp
    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;
    
    public SpecialOfferVO() {
    }
    
    public SpecialOfferVO(String name, Integer discountPercentage) {
        this.name = name;
        this.discountPercentage = discountPercentage;
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