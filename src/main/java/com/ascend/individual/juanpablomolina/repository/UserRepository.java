package com.ascend.individual.juanpablomolina.repository;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ascend.individual.juanpablomolina.entity.UserEntity;

/**
 * Repositorio para guardar las entidades de Usuarios en la base de datos.
 *
 *
 * @author juan.molina
 *
 */
public interface UserRepository extends JpaRepository<UserEntity, UUID> {

    UserEntity findByEmail(String email);

}
