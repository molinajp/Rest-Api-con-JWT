package com.ascend.individual.juanpablomolina.dto;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * DTO para respetar el contrato de salido requerido.
 *
 *
 * @author juan.molina
 *
 */
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ErrorsDto {

    /**
     * Una lista con cada excepción que tire la aplicación.
     */
    private List<ExceptionDto> errors;

    /**
     * Método para poder agregar de manera más cómoda los elementos al
     * campo errors.
     *
     * @param exception ExceptionDTO
     */
    public void addException(final ExceptionDto exception) {
        errors.add(exception);
    }

}
