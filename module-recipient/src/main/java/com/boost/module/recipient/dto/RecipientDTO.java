package com.boost.module.recipient.dto;

import com.boost.module.recipient.db.entity.RecipientVO;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
public class RecipientDTO {
    private UUID id;

    @NotBlank(message = "Name is required")
    private String name;

    @NotBlank(message = "Email is required")
    @Email(message = "Email should be valid")
    private String email;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    public RecipientDTO() {
    }

    public RecipientDTO(UUID id, String name, String email, LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public static RecipientDTO map(RecipientVO recipientVO) {
        RecipientDTO recipientDTO = new RecipientDTO();

        recipientDTO.setId(recipientVO.getId());
        recipientDTO.setName(recipientVO.getName());
        recipientDTO.setEmail(recipientVO.getEmail());

        return recipientDTO;
    }
}