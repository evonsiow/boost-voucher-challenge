package com.boost.module.recipient.model;

import com.boost.module.recipient.dto.RecipientDTO;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
public class RecipientResponseVM {
    private UUID id;
    private String name;
    private String email;

    public RecipientResponseVM() {
    }

    public RecipientResponseVM(UUID id, String name, String email, LocalDateTime createdAt) {
        this.id = id;
        this.name = name;
        this.email = email;
    }

    public static RecipientResponseVM map(RecipientDTO recipientDTO) {
        RecipientResponseVM recipientResponseVM = new RecipientResponseVM();

        recipientResponseVM.setId(recipientDTO.getId());
        recipientResponseVM.setName(recipientDTO.getName());
        recipientResponseVM.setEmail(recipientDTO.getEmail());

        return recipientResponseVM;
    }
}
