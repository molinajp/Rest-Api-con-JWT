package com.ascend.individual.juanpablomolina.service;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.ascend.individual.juanpablomolina.dto.UserDtoRequest;
import com.ascend.individual.juanpablomolina.dto.UserDtoResponse;
import com.ascend.individual.juanpablomolina.entity.UserEntity;
import com.ascend.individual.juanpablomolina.exception.TokenNotValidException;

/**
 * Interfaz para definir métodos útiles para la entidad User.
 *
 * <p>Estos métodos son los encargados de llevar a cabo la lógica de negocio que
 * tenga que ver con la entidad User
 *
 * @author juan.molina
 *
 */
public interface UserService {

    UserDtoResponse initAndSaveUser(UserDtoRequest user);

    UserEntity getUserByEmail(String email);

    Map<String, String> refreshToken(HttpServletRequest request, HttpServletResponse response)
            throws TokenNotValidException;

}
