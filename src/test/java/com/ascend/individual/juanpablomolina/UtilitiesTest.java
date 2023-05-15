package com.ascend.individual.juanpablomolina;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.when;

import java.util.UUID;

import javax.servlet.http.HttpServletRequest;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.ascend.individual.juanpablomolina.entity.UserEntity;
import com.ascend.individual.juanpablomolina.util.Utilities;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;

/**
 * Test para clase Utilities.
 *
 * @author juan.molina
 *
 */
@ExtendWith(MockitoExtension.class)
class UtilitiesTest {
    
    @Mock
    private HttpServletRequest request;
    
    private UserEntity userEntity;
    
    private StringBuffer stringBuffer;
    
    @BeforeEach
    void setUp() {
        userEntity = new UserEntity();
        stringBuffer = new StringBuffer("localhost:8080");
    }

    @Test
    @DisplayName("Test para crear token de acceso JWT")
    void testCreateJwtAccessToken() {
        userEntity.setId(new UUID(1L, 2L));
        when(request.getRequestURL()).thenReturn(stringBuffer);
        assertThat(Utilities.createJwtToken(userEntity, request, false)).isInstanceOf(String.class);
    }

    @Test
    @DisplayName("Test para crear token de refresco JWT")
    void testCreateJwtRefreshToken() {
        userEntity.setEmail("mail@mail.com");
        when(request.getRequestURL()).thenReturn(stringBuffer);
        assertThat(Utilities.createJwtToken(userEntity, request, true)).isInstanceOf(String.class);
    }

    @Test
    @DisplayName("Test para crear token JWT tira excepción")
    void testCreateJwtTokenThrowsException() {
        assertThat(Utilities.createJwtToken(userEntity, null, true)).isNull();
    }

    @Test
    @DisplayName("Test para decodear el token JWT OK")
    void testDecodeJWTToken() {
        userEntity.setId(new UUID(1L, 2L));
        when(request.getRequestURL()).thenReturn(stringBuffer);
        assertThat(Utilities.decodeToken(Utilities.createJwtToken(userEntity, request, false))).isInstanceOf(DecodedJWT.class);
    }

    @Test
    @DisplayName("Test para decodear el token JWT tira excepción")
    void testDecodeJWTTokenThrowsException() {
        assertThatThrownBy(() -> Utilities.decodeToken("Token malformado")).isInstanceOf(JWTVerificationException.class);
    }
}
