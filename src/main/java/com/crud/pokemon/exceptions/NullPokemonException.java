package com.crud.pokemon.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.io.Serial;
import java.io.Serializable;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class NullPokemonException extends RuntimeException implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    public NullPokemonException() {
        super("Pokemon doesn't exist in database!");
    }
    public NullPokemonException(String message) {
        super(message);
    }

}
