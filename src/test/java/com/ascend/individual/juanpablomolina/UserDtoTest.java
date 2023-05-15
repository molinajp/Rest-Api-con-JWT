package com.ascend.individual.juanpablomolina;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Set;
import java.util.stream.Stream;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;

import com.ascend.individual.juanpablomolina.dto.UserDtoRequest;
/**
 * Clase que se encarga de testear las validaciones del dto UserDTO.
 *
 * @author juan.molina
 *
 */

class UserDtoTest {

    /**
     * Campo para obtener la clase que corrobora los errores de validación, y
     * así realizar las pruebas.
     */
    private Validator validator;
    
    private UserDtoRequest userDto;

    /**
     * Se inicia el campo validator antes de comenzar las pruebas.
     */
    @BeforeEach
    void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
        userDto = new UserDtoRequest(null, "mail@mail.com", "Prueba01", null); 
    }

    @ParameterizedTest
    @ValueSource(strings = { "prueba01", "PrueBa02", "pRue0bA3" })
    @DisplayName("Test Parametrizado de UserDTO con problemas de contraseña por Mayúsculas")
    void createUserDtoWithBadPasswordDueToUppercase(String password) {
        userDto.setPassword(password);
        Set<ConstraintViolation<UserDtoRequest>> violations = validator.validate(userDto);
        assertThat(violations).hasSize(1);
    }

    @ParameterizedTest
    @ValueSource(strings = { "Pruebaaa", "Pruebaa0", "Prueba012", "pr0uRbaa", "pr0u0Rb0aa", "pr000uRbaa", "00pr0uRbaa", "000pruRbaa"})
    @DisplayName("Test Parametrizado de UserDTO con problemas de contraseña por Números")
    void createUserDtoWithBadPasswordDueToNumbers(String password) {
        userDto.setPassword(password);
        Set<ConstraintViolation<UserDtoRequest>> violations = validator.validate(userDto);
        assertThat(violations).hasSize(1);
    }

    @ParameterizedTest
    @ValueSource(strings = { "Prueba*01", "-Prueb3aa0", "02Prueba!" })
    @DisplayName("Test Parametrizado de UserDTO con problemas de contraseña por Caracteres Especiales")
    void createUserDtoWithBadPasswordDueToSpecialCharacters(String password) {
        userDto.setPassword(password);
        Set<ConstraintViolation<UserDtoRequest>> violations = validator.validate(userDto);
        assertThat(violations).hasSize(1);
    }

    @ParameterizedTest
    @ValueSource(strings = { "Prueb10", "Prrruee0eba1aaa"})
    @DisplayName("Test Parametrizado de UserDTO con problemas de contraseña por Longitud")
    void createUserDtoWithBadPasswordDueToLenght(final String password) {
        userDto.setPassword(password);
        Set<ConstraintViolation<UserDtoRequest>> violations = validator.validate(userDto);
        assertThat(violations).hasSize(1);
    }

    @Test
    @DisplayName("Test de UserDTO con problemas de contraseña nula")
    void createUserDtoWithBadPasswordDueToNullValue() {
        userDto.setPassword(null);
        Set<ConstraintViolation<UserDtoRequest>> violations = validator.validate(userDto);
        assertThat(violations).hasSize(1);
    }

    @ParameterizedTest
    @ValueSource(strings = { "mailmail.com", "mail@mailcom", "mas.mail.com"})
    @DisplayName("Test Parametrizado de UserDTO con problemas de email por formato")
    void createUserDtoWithBadEmail(final String email) {
        userDto.setEmail(email);
        Set<ConstraintViolation<UserDtoRequest>> violations = validator.validate(userDto);
        assertThat(violations).hasSize(1);
    }

    @Test
    @DisplayName("Test de UserDTO con problemas de email nulo")
    void createUserDtoWithBadEmailDueToNullValue() {
        userDto.setEmail(null);
        Set<ConstraintViolation<UserDtoRequest>> violations = validator.validate(userDto);
        assertThat(violations).hasSize(1);
    }

    @ParameterizedTest
    @MethodSource("provideParameters")
    @DisplayName("Test Parametrizado de UserDTO OK")
    void createUserDtoOK(String email, String password) {
        userDto.setPassword(password);
        userDto.setEmail(email);
        Set<ConstraintViolation<UserDtoRequest>> violations = validator.validate(userDto);
        assertTrue(violations.isEmpty());
    }

    private static Stream<Arguments> provideParameters() {
        return Stream.of(Arguments.of("test@gmail.com", "Passw0rd1"),
                         Arguments.of("test2@hotmail.com", "alGo9por7que"),
                         Arguments.of("test3@globallogic.com", "1asegr6T"));
    }

}
