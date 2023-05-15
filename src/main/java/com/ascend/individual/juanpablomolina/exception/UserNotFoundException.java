package com.ascend.individual.juanpablomolina.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Excepción para tirar cuando se buca un usuario por email de un usuario y no
 * existe en la base de datos.
 *
 * @author juan.molina
 *
 */
@ResponseStatus(HttpStatus.NOT_FOUND)
public class UserNotFoundException extends Exception {

    private static final long serialVersionUID = 2461547935659344532L;

    public UserNotFoundException(final String message) {
        super(message);
    }

}
