package com.ascend.individual.juanpablomolina.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Excepción para tirar cuando el email de un usuario ya exista en la base de
 * datos.
 *
 *
 * @author juan.molina
 *
 */
@ResponseStatus(HttpStatus.BAD_REQUEST)
public class UserEmailNotUniqueException extends RuntimeException {

    private static final long serialVersionUID = -875917576558141555L;

    public UserEmailNotUniqueException(final String message) {
        super(message);
    }
}
