package com.crud.pokemon.model.enums;

import lombok.Getter;

@Getter
public enum Role {

    ADMIN("admin"),
    USER("user");

    private String userRole;

    Role(String role) {
        this.userRole = role;
    }
}
