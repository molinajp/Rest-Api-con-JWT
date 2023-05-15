package com.ascend.individual.juanpablomolina;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.BDDMockito;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.ascend.individual.juanpablomolina.dto.PhoneDtoRequest;
import com.ascend.individual.juanpablomolina.dto.UserDtoRequest;
import com.ascend.individual.juanpablomolina.entity.PhoneEntity;
import com.ascend.individual.juanpablomolina.entity.UserEntity;
import com.ascend.individual.juanpablomolina.exception.TokenNotValidException;
import com.ascend.individual.juanpablomolina.exception.UserEmailNotUniqueException;
import com.ascend.individual.juanpablomolina.repository.UserRepository;
import com.ascend.individual.juanpablomolina.serviceimpl.UserServiceImpl;
import com.ascend.individual.juanpablomolina.util.Utilities;
import com.auth0.jwt.interfaces.DecodedJWT;

/**
 * Tests para UserService.
 *
 * @author juan.molina
 *
 */
@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @InjectMocks
    private UserServiceImpl userService;

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    private UserEntity user;

    private UserDtoRequest userDto;

    @BeforeEach
    void setUp() {
        HashSet<PhoneDtoRequest> phonesDto = new HashSet<>(Set.of(new PhoneDtoRequest()));
        HashSet<PhoneEntity> phonesEntity = new HashSet<>(Set.of(new PhoneEntity()));
        String name = "Juan";
        String email = "mail@mail.com";
        String password = "12Pasdfg";
        Date date = new Date();
        user = new UserEntity(null, date, date, name, email, password, phonesEntity, true);
        userDto = new UserDtoRequest(name, email, password, phonesDto);
    }

    @Test
    @DisplayName("Test de método getUserByEmail")
    void testGetUserByEmailOk() {
        String email = "";
        userService.getUserByEmail(email);
        verify(userRepository).findByEmail(email);
    }

    @Test
    @DisplayName("Test de método loadUserByUsername throwing exception")
    void testLoadUserByUsernameThrowsException() {
        BDDMockito.given(userRepository.findByEmail(anyString())).willReturn(null);
        assertThatThrownBy(() -> userService.loadUserByUsername("")).isInstanceOf(UsernameNotFoundException.class)
                .hasMessageContaining("User not found");
    }

    @Test
    @DisplayName("Test de método loadUserByUsername OK")
    void testLoadUserByUsernameReturnsUser() {
        BDDMockito.given(userRepository.findByEmail(anyString())).willReturn(user);
        assertThat(userService.loadUserByUsername(anyString())).hasFieldOrPropertyWithValue("username", user.getEmail())
                .hasFieldOrPropertyWithValue("password", user.getPassword());
    }

    @Test
    @DisplayName("Test de método initAndSaveUser that throws Exception")
    void testInitAndSaveUserThrowsException() {
        UserDtoRequest userDto = new UserDtoRequest(null, "mail@mail.com", "12Pasdfg", null);
        UserEntity user = new UserEntity(null, new Date(), new Date(), null, "mail@mail.com", "12Pasdfg", null, true);
        BDDMockito.given(userRepository.findByEmail(anyString())).willReturn(user);
        assertThatThrownBy(() -> userService.initAndSaveUser(userDto)).isInstanceOf(UserEmailNotUniqueException.class)
                .hasMessageContaining("Email " + user.getEmail() + " it's in use");
    }

    @Test
    @DisplayName("Test de método initAndSaveUser OK")
    void testInitAndSaveUserReturnsUser() {
        given(userRepository.findByEmail(anyString())).willReturn(null);
        given(passwordEncoder.encode(anyString())).willReturn(userDto.getPassword());
        given(userRepository.save(any())).willReturn(user);
        userService.initAndSaveUser(userDto);
        ArgumentCaptor<UserEntity> argumentCaptor = ArgumentCaptor.forClass(UserEntity.class);
        verify(userRepository).save(argumentCaptor.capture());
        UserEntity capturedUser = argumentCaptor.getValue();
        assertThat(capturedUser).hasNoNullFieldsOrPropertiesExcept("id")
                .hasFieldOrPropertyWithValue("email", user.getEmail())
                .hasFieldOrPropertyWithValue("password", user.getPassword())
                .hasFieldOrPropertyWithValue("name", user.getName())
                .hasFieldOrPropertyWithValue("phones", user.getPhones()).hasFieldOrPropertyWithValue("id", user.getId())
                .hasFieldOrPropertyWithValue("isActive", user.isActive());

    }

    @Test
    @DisplayName("Test de método getJwtTokenForUser that throws Exception")
    void testGetJwtTokenForUserThrowsException() {
        assertThat(userService.getTokenForUser(user)).isNull();
    }

    @Test
    @DisplayName("Test de método getJwtTokenForUser OK")
    void testGetJwtTokenForUserOk() {
        try (MockedStatic<Utilities> utilities = Mockito.mockStatic(Utilities.class)) {
            utilities.when(() -> Utilities.createJwtToken(user, null, false)).thenReturn("");
            assertThat(userService.getTokenForUser(user)).isInstanceOf(String.class);
        }
    }

    @Test
    @DisplayName("Test de método getRefreshTokenForUser that throws Exception")
    void testGetRefreshTokenForUserThrowsException() {
        assertThatThrownBy(() -> userService.refreshToken(new MockHttpServletRequest(), new MockHttpServletResponse()))
                .isInstanceOf(TokenNotValidException.class);
    }

    @Test
    @DisplayName("Test de método getRefreshTokenForUser OK")
    void testGetRefreshTokenForUser() throws TokenNotValidException {
        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);
        DecodedJWT decodedJWT = mock(DecodedJWT.class);
        String authorizationHeader = "Bearer token.JWT.test";
        String refreshTokenExpected = "token.JWT.test";
        String accessTokenExpected = "accessToken";
        try (MockedStatic<Utilities> utilities = Mockito.mockStatic(Utilities.class)) {
            when(request.getHeader("Authorization")).thenReturn(authorizationHeader);
            when(Utilities.decodeToken(anyString())).thenReturn(decodedJWT);
            when(decodedJWT.getSubject()).thenReturn(user.getEmail());
            when(userService.getUserByEmail(user.getEmail())).thenReturn(user);
            when(Utilities.createJwtToken(user, request, false)).thenReturn(accessTokenExpected);

            assertThat(userService.refreshToken(request, response)).containsAllEntriesOf(
                    new HashMap<>(Map.of("accessToken", accessTokenExpected, "refreshToken", refreshTokenExpected)));
        }
    }
}
