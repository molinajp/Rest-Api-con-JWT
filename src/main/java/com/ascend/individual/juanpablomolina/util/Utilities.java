package com.ascend.individual.juanpablomolina.util;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;

import com.ascend.individual.juanpablomolina.dto.PhoneDtoRequest;
import com.ascend.individual.juanpablomolina.dto.PhoneDtoResponse;
import com.ascend.individual.juanpablomolina.dto.UserDtoRequest;
import com.ascend.individual.juanpablomolina.dto.UserDtoResponse;
import com.ascend.individual.juanpablomolina.entity.PhoneEntity;
import com.ascend.individual.juanpablomolina.entity.UserEntity;
import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.auth0.jwt.interfaces.JWTVerifier;

/**
 * Clase para no repetir código en varios lugares al momento de generar el token
 * JWT para cada usuario.
 *
 * @author juan.molina
 *
 */
public class Utilities {
    
    private static final String SECRET_KEY = "claveSecreta";

    private static final int EXPIRY_TIME_FOR_ACCESS_TOKEN = 10 * 60 * 1000;

    private static final int EXPIRY_TIME_FOR_REFRESH_TOKEN = 60 * 60 * 1000;
    
    private Utilities() {
        
    }

    public static String createJwtToken(UserEntity user, HttpServletRequest request, boolean isRefreshToken) {
        if (request != null) {
            var algorithm = Algorithm.HMAC256(SECRET_KEY.getBytes());
            if (!isRefreshToken) {
                return JWT.create().withSubject(user.getId().toString())
                        .withExpiresAt(new Date(System.currentTimeMillis() + EXPIRY_TIME_FOR_ACCESS_TOKEN))
                        .withIssuer(request.getRequestURL().toString()).withClaim("roles", "ROLE_ADMIN")
                        .sign(algorithm);
            } else {
                return JWT.create().withSubject(user.getEmail())
                        .withExpiresAt(new Date(System.currentTimeMillis() + EXPIRY_TIME_FOR_REFRESH_TOKEN))
                        .withIssuer(request.getRequestURL().toString()).sign(algorithm);
            }
        } else {
            return null;
        }
    }

    public static DecodedJWT decodeToken(final String token) {
        var algorithm = Algorithm.HMAC256(SECRET_KEY.getBytes());
        JWTVerifier verifier = JWT.require(algorithm).build();
        return verifier.verify(token);
    }

    public static PhoneEntity mapPhoneDtoRequestToPhoneEntity(PhoneDtoRequest phoneDtoRequest) {
        if (phoneDtoRequest != null) {
            return PhoneEntity.builder().number(phoneDtoRequest.getNumber()).cityCode(phoneDtoRequest.getCityCode())
                    .countryCode(phoneDtoRequest.getCountryCode()).build();
        } else {
            return null;
        }
    }

    public static Set<PhoneEntity> mapPhoneDtoRequestToPhoneEntity(Set<PhoneDtoRequest> phoneDtoRequest) {
        if (phoneDtoRequest != null) {
            return phoneDtoRequest.stream()
                                  .map(Utilities::mapPhoneDtoRequestToPhoneEntity)
                                  .collect(Collectors.toSet());
        } else {
            return  new HashSet<>();
        }
    }

    public static PhoneDtoResponse mapPhoneEntityToPhoneDtoResponse(PhoneEntity phoneEntity) {
        if (phoneEntity != null) {
            return PhoneDtoResponse.builder().number(phoneEntity.getNumber()).cityCode(phoneEntity.getCityCode())
                    .countryCode(phoneEntity.getCountryCode()).build();
        } else {
            return null;
        }
    }

    public static Set<PhoneDtoResponse> mapPhoneEntityToPhoneDtoResponse(Set<PhoneEntity> phoneEntity) {
        if (phoneEntity != null) {
            return phoneEntity.stream()
                              .map(Utilities::mapPhoneEntityToPhoneDtoResponse)
                              .collect(Collectors.toSet());
        } else {
            return new HashSet<>();
        }
    }
    
    public static UserEntity mapUserDtoRequestToUserEntity(UserDtoRequest userDto) {
        return UserEntity.builder().email(userDto.getEmail()).name(userDto.getName())
                .password(userDto.getPassword()).created(new Date()).lastLogin(new Date())
                .isActive(true).phones(Utilities.mapPhoneDtoRequestToPhoneEntity(userDto.getPhones())).build();
    }

    public static UserDtoResponse mapUserEntityToUserDtoResponse(UserEntity user) {
        if (user != null) {
            return UserDtoResponse.builder().id(user.getId()).created(user.getCreated()).lastLogin(user.getLastLogin())
                    .name(user.getName()).email(user.getEmail()).password(user.getPassword())
                    .phones(mapPhoneEntityToPhoneDtoResponse(user.getPhones())).isActive(user.isActive()).build();
        } else {
            return null;
        }
    }
}
