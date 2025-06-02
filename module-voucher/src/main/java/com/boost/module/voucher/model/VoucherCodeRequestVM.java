package com.boost.module.voucher.model;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
public class VoucherCodeRequestVM {
    @NotNull(message = "Offer ID is required")
    private UUID offerId;

    @NotEmpty(message = "At least one recipient email is required")
    private List<@Email(message = "All emails must be valid") String> recipientEmails;

    @NotNull(message = "Expiration date is required")
    @Future(message = "Expiration date must be in the future")
    private LocalDateTime expirationDate;

    public VoucherCodeRequestVM() {
    }

    public VoucherCodeRequestVM(UUID offerId, List<String> recipientEmails, LocalDateTime expirationDate) {
        this.offerId = offerId;
        this.recipientEmails = recipientEmails;
        this.expirationDate = expirationDate;
    }
}
