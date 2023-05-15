package com.ascend.individual.juanpablomolina.controller;

import java.io.IOException;
import java.net.URI;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.ascend.individual.juanpablomolina.dto.UserDtoRequest;
import com.ascend.individual.juanpablomolina.dto.UserDtoResponse;
import com.ascend.individual.juanpablomolina.exception.TokenNotValidException;
import com.ascend.individual.juanpablomolina.service.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.RequiredArgsConstructor;

/**
 * Controlador donde se encuentran los endpoints.
 *
 * @author juan.molina
 *
 */
@RestController
@RequiredArgsConstructor
public class Controller {

    private final UserService userService;

    /**
     * Endpoint para registrar usuario.
     *
     * <p>Recibe un JSON con los datos del usuario, y si pasa las validaciones procede
     * a insertarse en la base de datos
     * 
     * <p>Los usuarios no pueden tener el email repetido
     *
     * @param user UserDto
     * @return ResponseEntity
     */
    @PostMapping("/sign_up")
    public ResponseEntity<UserDtoResponse> signUpUser(@RequestBody @Valid UserDtoRequest user) {
        UserDtoResponse userToReturn = userService.initAndSaveUser(user);
        var uri = URI.create(ServletUriComponentsBuilder.fromCurrentRequest().toUriString());
        return ResponseEntity.created(uri).body(userToReturn);
    }

    /**
     * Endpoint para renovar el token JWT.
     *
     * <p>En caso de que el token JWT expire se puede renovar a través de este método
     *
     * @param request  HttpServletRequest
     * @param response HttpServletResponse
     * @throws TokenNotValidException Si el token JWT no es válido
     */
    @GetMapping("/refreshToken")
    public void refreshToken(HttpServletRequest request, HttpServletResponse response)
            throws IOException, TokenNotValidException {
        new ObjectMapper().writeValue(response.getOutputStream(), userService.refreshToken(request, response));
    }

}
