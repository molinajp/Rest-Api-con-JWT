package com.ascend.individual.juanpablomolina.security;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.ascend.individual.juanpablomolina.dto.ErrorsDto;
import com.ascend.individual.juanpablomolina.dto.ExceptionDto;
import com.ascend.individual.juanpablomolina.entity.UserEntity;
import com.ascend.individual.juanpablomolina.exception.UserNotFoundException;
import com.ascend.individual.juanpablomolina.repository.UserRepository;
import com.ascend.individual.juanpablomolina.util.Utilities;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.RequiredArgsConstructor;

/**
 * Clase para la autenticación de los usuarios.
 *
 * @author juan.molina
 *
 */
@RequiredArgsConstructor
public class CustomAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    private final AuthenticationManager authenticationManager;

    private final UserRepository userRepository;

    /**
     * Método para realizar la autenticación del User. Se toma el JSON enviado por
     * el cliente y lo mapea a la entidad User, se busca en el repositorio y en caso
     * de no existir se tira la excepción UserNotFoundException, y en caso de ser
     * encontrado, se prosigue con la autenticación
     *
     * @param request  HttpServletRequest
     * @param response HttpServletResponse
     * @return Authentication
     * @throws AuthenticationException Excepción lanzada por Spring Security.
     */
    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response)
            throws AuthenticationException {
        try {
            UserEntity user = new ObjectMapper().readValue(request.getInputStream(), UserEntity.class);
            UserEntity userFromDb = userRepository.findByEmail(user.getEmail());
            if (userFromDb != null && userFromDb.isActive()) {
                var authenticationToken = new UsernamePasswordAuthenticationToken(user.getEmail(), user.getPassword());
                return authenticationManager.authenticate(authenticationToken);
            } else {
                throw new UserNotFoundException("User with email " + user.getEmail() + " not found");
            }
        } catch (IOException | UserNotFoundException | AuthenticationException e) {
            var errors = new ErrorsDto(new ArrayList<>());
            var errorDetails = new ExceptionDto(new Date(), HttpStatus.FORBIDDEN.value(), e.getMessage());
            errors.addException(errorDetails);
            response.setStatus(HttpStatus.FORBIDDEN.value());
            response.setContentType("application/json");
            try {
                new ObjectMapper().writeValue(response.getOutputStream(), errors);
            } catch (IOException e1) {
                e1.printStackTrace();
            }
        }
        return null;
    }

    /**
     * Este método se ejecuta si la autenticación es exitosa.
     *
     * <p>Se recupera el usuario desde Spring Security y se recuperan todos los datos
     * desde la base de datos, luego se generan los token JWT para acceso y
     * refresco, y actualiza la fecha de último login del usuario para finalmente
     * devolver el objeto como JSON
     *
     * @param request        HttpServletRequest
     * @param response       HttpServletResponse
     * @param chain          FilterChain
     * @param authentication Authentication
     * @throws IOException      Excepción lanzada por Spring Security
     * @throws ServletException Excepción lanzada por Spring Security.
     */
    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain,
            Authentication authentication) throws IOException, ServletException {
        var userFromRequest = (User) authentication.getPrincipal();
        UserEntity user = userRepository.findByEmail(userFromRequest.getUsername());
        String accessToken = Utilities.createJwtToken(user, request, false);
        String refreshToken = Utilities.createJwtToken(user, request, true);
        user.setLastLogin(new Date());
        var userToReturn = Utilities.mapUserEntityToUserDtoResponse(userRepository.save(user));
        userToReturn.setToken(accessToken);
        response.setHeader("refreshToken", refreshToken);
        response.setContentType("application/json");
        new ObjectMapper().writeValue(response.getOutputStream(), userToReturn);
    }

}
