package com.ascend.individual.juanpablomolina.serviceimpl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.ascend.individual.juanpablomolina.dto.UserDtoRequest;
import com.ascend.individual.juanpablomolina.dto.UserDtoResponse;
import com.ascend.individual.juanpablomolina.entity.UserEntity;
import com.ascend.individual.juanpablomolina.exception.TokenNotValidException;
import com.ascend.individual.juanpablomolina.exception.UserEmailNotUniqueException;
import com.ascend.individual.juanpablomolina.repository.UserRepository;
import com.ascend.individual.juanpablomolina.service.UserService;
import com.ascend.individual.juanpablomolina.util.Utilities;

import lombok.RequiredArgsConstructor;

/**
 * Clase que implementa todos los métodos de la interfaz UserService, y que se
 * encargar de la lógica de negocio a implementar en cada uno de los endpoints.
 *
 * @author juan.molina
 *
 */
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService, UserDetailsService {

    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;

    /**
     * Método que se sobreescribe para customizar el flujo para autorizar a los
     * usuarios.
     *
     * @param email String
     * @return UserDetails
     */
    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        UserEntity user = userRepository.findByEmail(email);
        if (user == null) {
            throw new UsernameNotFoundException("User not found");
        }
        Collection<SimpleGrantedAuthority> authorities = new ArrayList<>();
        authorities.add(new SimpleGrantedAuthority("ROLE_ADMIN"));
        return new User(user.getEmail(), user.getPassword(), authorities);
    }

    /**
     * Método para obtener un usuario con su mail.
     *
     * @param email String
     * @return User
     */
    public UserEntity getUserByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    /**
     * Método para iniciar y guardar un usuario en la base de datos, y otorgarle un
     * token JWT de acceso.
     *
     * @param userDto UserDTO
     * @return User
     */
    public UserDtoResponse initAndSaveUser(final UserDtoRequest userDto) {
        if (userRepository.findByEmail(userDto.getEmail()) != null) {
            throw new UserEmailNotUniqueException("Email " + userDto.getEmail() + " it's in use");
        } else {
            userDto.setPassword(passwordEncoder.encode(userDto.getPassword()));
            var userToSave = Utilities.mapUserDtoRequestToUserEntity(userDto);
            var userSaved = userRepository.save(userToSave);
            var token = getTokenForUser(userSaved);
            var userToReturn = Utilities.mapUserEntityToUserDtoResponse(userSaved);
            userToReturn.setToken(token);
            return userToReturn;
        }
    }

    /**
     * Metodo para agregar token JWT al usuario.
     *
     * @param UserEntity user
     * @return String
     */
    public String getTokenForUser(UserEntity user) {
        var requestAttributes = RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = requestAttributes != null
                ? ((ServletRequestAttributes) requestAttributes).getRequest()
                : null;
        return Utilities.createJwtToken(user, request, false);
    }

    /**
     * Método para refrescar el token de acceso en caso de que este haya expirado.
     * 
     * @param request  HttpServletRequest
     * @param response HttpServletResponse
     * @return Map
     * @throws TokenNotValidException Si el token no es válido
     */
    public Map<String, String> refreshToken(HttpServletRequest request, HttpServletResponse response)
            throws TokenNotValidException {
        var authorizationHeader = request.getHeader("Authorization");
        var prefix = "Bearer ";
        if (authorizationHeader != null && authorizationHeader.startsWith(prefix)) {
            var refreshToken = authorizationHeader.substring(prefix.length());
            var decodedJWT = Utilities.decodeToken(authorizationHeader.substring(prefix.length()));
            var email = decodedJWT.getSubject();
            var user = getUserByEmail(email);
            var accessToken = Utilities.createJwtToken(user, request, false);
            Map<String, String> tokens = new HashMap<>();
            tokens.put("accessToken", accessToken);
            tokens.put("refreshToken", refreshToken);
            response.setContentType("application/json");
            return tokens;
        } else {
            throw new TokenNotValidException("Refresh token is missing");
        }
    }
}
