package com.boost.module.voucher.db;

import com.boost.module.voucher.db.entity.VoucherCodeVO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface VoucherCodeRepository extends JpaRepository<VoucherCodeVO, UUID> {
    Optional<VoucherCodeVO> findByCode(String code);
    
    List<VoucherCodeVO> findByRecipientIdAndUsedDateIsNullAndExpirationDateAfter(UUID recipientId, LocalDateTime now);
    
    boolean existsByCode(String code);
} 