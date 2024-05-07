package com.crud.pokemon.model.enums;

import lombok.Getter;

@Getter
public enum Role {

    ADMIN("admin"),
    USER("user");

    private String role;

    Role(String role) {
        this.role = role;
    }
}
