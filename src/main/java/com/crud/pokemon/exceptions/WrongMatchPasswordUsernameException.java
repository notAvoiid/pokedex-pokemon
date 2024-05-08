package com.crud.pokemon.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.io.Serial;
import java.io.Serializable;

@ResponseStatus(HttpStatus.UNAUTHORIZED)
public class WrongMatchPasswordUsernameException extends RuntimeException  implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    public WrongMatchPasswordUsernameException() {
        super("The username or password is incorrect!");
    }

    public WrongMatchPasswordUsernameException(String message) {
        super(message);
    }
}
