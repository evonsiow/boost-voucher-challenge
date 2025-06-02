package com.boost.module.voucher.db.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
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
@Table(name = "voucher_codes")
public class VoucherCodeVO {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id", nullable = false)
    private UUID id;

    @NotBlank(message = "Code is required")
    @Column(name = "code", nullable = false, unique = true, length = 20)
    private String code;

    @NotNull(message = "Recipient ID is required")
    @Column(name = "recipient_id", nullable = false)
    private UUID recipientId;

    @NotNull(message = "Special offer ID is required")
    @Column(name = "special_offer_id", nullable = false)
    private UUID specialOfferId;

    @NotNull(message = "Expiration date is required")
    @Column(name = "expiration_date", nullable = false)
    private LocalDateTime expirationDate;

    @Column(name = "used_date")
    private LocalDateTime usedDate;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    public VoucherCodeVO() {
    }

    public VoucherCodeVO(String code, UUID recipientId, UUID specialOfferId, LocalDateTime expirationDate) {
        this.code = code;
        this.recipientId = recipientId;
        this.specialOfferId = specialOfferId;
        this.expirationDate = expirationDate;
    }
}
