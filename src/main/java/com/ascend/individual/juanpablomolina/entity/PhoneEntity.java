package com.ascend.individual.juanpablomolina.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Entidad de Teléfonos.
 *
 * <p>Esta clase es la entidad con la que se mapea cada teléfono a la tabla
 * correspondiente(PHONES) en la base de datos
 *
 * @author juan.molina
 *
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "PHONES")
public class PhoneEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    private long id;

    @Column(name = "NUMBER")
    private long number;

    @Column(name = "CITY_CODE")
    private int cityCode;

    @Column(name = "COUNTRY_CODE")
    private String countryCode;

}
