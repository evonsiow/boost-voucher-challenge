package com.boost.module.recipient.model;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RecipientRequestVM {
    @NotBlank(message = "Name is required")
    private String name;

    @NotBlank(message = "Email is required")
    @Email(message = "Email should be valid")
    private String email;

    public RecipientRequestVM() {
    }

    public RecipientRequestVM(String name, String email) {
        this.name = name;
        this.email = email;
    }
}
