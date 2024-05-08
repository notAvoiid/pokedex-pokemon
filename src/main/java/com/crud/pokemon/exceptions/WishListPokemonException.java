package com.crud.pokemon.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.io.Serial;
import java.io.Serializable;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class WishListPokemonException extends RuntimeException  implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    public WishListPokemonException() {
        super("Try again later!");
    }

    public WishListPokemonException(String message) {
        super(message);
    }
}
