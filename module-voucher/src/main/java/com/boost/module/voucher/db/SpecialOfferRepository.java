package com.boost.module.voucher.db;

import com.boost.module.voucher.db.entity.SpecialOfferVO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface SpecialOfferRepository extends JpaRepository<SpecialOfferVO, UUID> {
    Optional<SpecialOfferVO> findByName(String name);

    boolean existsByName(String name);
} 