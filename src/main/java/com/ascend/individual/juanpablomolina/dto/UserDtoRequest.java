package com.ascend.individual.juanpablomolina.dto;

import java.util.Set;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Clase para manejar los datos enviados por JSON.
 *
 * <p>Esta clase estipula reglas a seguir para algunos de los campos, para cumplir
 * con los requisitos estipulados
 *
 * @author juan.molina
 *
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserDtoRequest {

    private static final int MIN_SIZE_FOR_PASSWORD = 8;

    private static final int MAX_SIZE_FOR_PASSWORD = 12;

    /**
     * Este campo es opcional y corresponde al nombre del usuario.
     *
     */
    private String name;

    /** Este campo es obligatorio y corresponde al email del usuario.
     * Debe segir los estándares de mail y no ser nulo
     */
    @Pattern(regexp = ".+@.+\\..+", message = "Please provide a valid email address")
    @NotNull
    private String email;

    /**Este campo es obligatorio y corresponde a la contraseña del usuario. Debe tener un largo de entre MIN_SIZE_FOR_PASSWORD y
     * MAX_SIZE_FOR_PASSWORD, no permite caracteres especiales, solo letras y números, una sola letra mayúscula y dos números
     */
    @Size(min = MIN_SIZE_FOR_PASSWORD, max = MAX_SIZE_FOR_PASSWORD, message = "Password must have a length between 8 and 12 characters")
    @Pattern(regexp = "^[a-zA-Z0-9]*$", message = "Only letters and digits are allowed")
    @Pattern(regexp = "^(\\D*\\d\\D*){2}$", message = "The value should have 2 numbers")
    @Pattern(regexp = "^(?=.*[^A-Z])[^A-Z]*[A-Z][^A-Z]*$", message = "The value entered should have 1 uppercase letter")
    @NotNull
    private String password;

    /**
     * Este campo es opcional y corresponde a los teléfonos del usuario.
     */
    private Set<PhoneDtoRequest> phones;
}
