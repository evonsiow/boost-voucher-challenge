package com.boost.module.recipient.db.entity;

import com.boost.module.recipient.dto.RecipientDTO;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@Entity
@Table(name = "recipients")
public class RecipientVO {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id", nullable = false)
    private UUID id;

    @NotBlank(message = "Name is required")
    @Column(name = "name", nullable = false)
    private String name;

    @NotBlank(message = "Email is required")
    @Email(message = "Email should be valid")
    @Column(name = "email", nullable = false, unique = true)
    private String email;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    public RecipientVO() {
    }

    public RecipientVO(String name, String email) {
        this.name = name;
        this.email = email;
    }

    public static RecipientDTO map(RecipientVO recipientVO) {
        RecipientDTO recipientDTO = new RecipientDTO();

        recipientDTO.setId(recipientVO.getId());
        recipientDTO.setName(recipientVO.getName());
        recipientDTO.setEmail(recipientVO.getEmail());
        recipientDTO.setCreatedAt(recipientVO.getCreatedAt());
        recipientDTO.setUpdatedAt(recipientVO.getUpdatedAt());

        return recipientDTO;
    }
} 