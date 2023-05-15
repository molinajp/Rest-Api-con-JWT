package com.ascend.individual.juanpablomolina.security;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import com.ascend.individual.juanpablomolina.dto.ErrorsDto;
import com.ascend.individual.juanpablomolina.dto.ExceptionDto;
import com.ascend.individual.juanpablomolina.util.Utilities;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.AllArgsConstructor;

/**
 * Clase para la autorización en la aplicación.
 *
 * @author juan.molina
 *
 */

@AllArgsConstructor
public class CustomAuthorizationFilter extends OncePerRequestFilter {

    /**
     * Método que se encarga de la autorización de los usuarios.
     *
     * <p>Todos las peticiones a la API pasan por este método, si la URI es una de las
     * indicadas en el primer if, entonces simplemente se deja seguir el flujo, pero
     * en caso de que sea un endpoint para el que se requiere autorización, entonces
     * se procede a hacer las validaciones del token JWT enviado en el header de la
     * request
     *
     *
     * @param request     HttpServletRequest
     * @param response    HttpServletResponse
     * @param filterChain FilterChain
     * @throws IOException      Excepción lanzada por Spring Security
     * @throws ServletException Excepción lanzada por Spring Security
     */

    @Override
    public void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        if (request.getServletPath().equals("/api/login") || request.getServletPath().equals("/api/sign_up")
                || request.getServletPath().equals("/api/refreshToken")) {
            filterChain.doFilter(request, response);
        } else {
            var authorizationHeader = request.getHeader("Authorization");
            var prefix = "Bearer ";
            if (authorizationHeader != null && authorizationHeader.startsWith(prefix)) {
                try {
                    var token = authorizationHeader.substring(prefix.length());
                    var decodedJWT = Utilities.decodeToken(token);
                    String username = decodedJWT.getSubject();
                    Collection<SimpleGrantedAuthority> authorities = new ArrayList<>();
                    authorities.add(new SimpleGrantedAuthority(decodedJWT.getClaim("roles").toString()));
                    var authenticationToken = new UsernamePasswordAuthenticationToken(username, null, authorities);
                    SecurityContextHolder.getContext().setAuthentication(authenticationToken);
                    filterChain.doFilter(request, response);
                } catch (Exception e) {
                    var errors = new ErrorsDto(new ArrayList<>());
                    var errorDetails = new ExceptionDto(new Date(), HttpStatus.FORBIDDEN.value(),
                            e.getMessage());
                    errors.addException(errorDetails);
                    response.setStatus(HttpStatus.FORBIDDEN.value());
                    response.setContentType("application/json");
                    new ObjectMapper().writeValue(response.getOutputStream(), errors);
                }
            } else {
                filterChain.doFilter(request, response);
            }
        }
    }
}
