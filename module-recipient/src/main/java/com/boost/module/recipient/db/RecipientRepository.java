package com.boost.module.recipient.db;

import com.boost.module.recipient.db.entity.RecipientVO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface RecipientRepository extends JpaRepository<RecipientVO, UUID> {
    Optional<RecipientVO> findByEmail(String email);

    boolean existsByEmail(String email);

    boolean existsByEmailAndIdNot(String email, UUID id);
} 