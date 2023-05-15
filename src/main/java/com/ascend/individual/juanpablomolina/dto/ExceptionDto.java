package com.ascend.individual.juanpablomolina.dto;

import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * DTO para respetar el contrato de salida de cada excepción.
 *
 *
 * @author juan.molina
 *
 */
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class ExceptionDto {

    /**
     * Timestamp en que se tira la excepción.
     */
    private Date timestamp;

    /**
     * Código HTTP que acompaña a la excepción.
     */
    private int code;

    /**
     * Detalles de la excepcón.
     */
    private String details;

}
