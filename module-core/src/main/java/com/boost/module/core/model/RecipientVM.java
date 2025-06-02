package com.boost.module.core.model;

import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
public class RecipientVM {
    private UUID id;

    private String name;

    private String email;

    public RecipientVM() {
    }

    public RecipientVM(UUID id, String name, String email) {
        this.id = id;
        this.name = name;
        this.email = email;
    }
}