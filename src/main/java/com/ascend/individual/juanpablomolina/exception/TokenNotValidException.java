package com.ascend.individual.juanpablomolina.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Excepción para tirar cuando el token JWT no sea válido o contenga errores.
 *
 *
 * @author juan.molina
 *
 */
@ResponseStatus(HttpStatus.BAD_REQUEST)
public class TokenNotValidException extends Exception {

    private static final long serialVersionUID = -3609914931997702010L;

    public TokenNotValidException(final String message) {
        super(message);
    }

}
