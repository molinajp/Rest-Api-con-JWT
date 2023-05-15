package com.ascend.individual.juanpablomolina.entity;

import java.util.Date;
import java.util.Set;
import java.util.UUID;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Type;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Entidad de Usuario.
 *
 * <p>Esta clase es la entidad con la que se mapea cada usuario a la tabla
 * correspondiente(USERS) en la base de datos
 *
 * @author juan.molina
 *
 */

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "USERS")
public class UserEntity {

    @Id
    @GeneratedValue(generator = "UUID", strategy = GenerationType.AUTO)
    @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
    @Type(type = "org.hibernate.type.UUIDCharType")
    @Column(name = "ID")
    private UUID id;

    @Column(name = "CREATED")
    private Date created;

    @Column(name = "LAST_LOGIN")
    private Date lastLogin;

    @Column(name = "NAME")
    private String name;

    /**
     * Email del usuario. Se utiliza para diferenciar a los usuarios de la misma
     * manera que en otras aplicaciones se diferencian a los usuarios por nombre de
     * usuario o nickname
     */
    @Column(name = "EMAIL")
    private String email;

    /**
     * Contraseña del usuario. Está encriptada en la base de datos
     */
    @Column(name = "PASSWORD")
    private String password;

    @OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinColumn(name = "USER_ID")
    private Set<PhoneEntity> phones;

    @Column(name = "IS_ACTIVE")
    private boolean isActive;

}
