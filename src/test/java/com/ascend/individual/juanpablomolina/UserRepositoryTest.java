package com.ascend.individual.juanpablomolina;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertNull;

import java.util.Date;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import com.ascend.individual.juanpablomolina.entity.UserEntity;
import com.ascend.individual.juanpablomolina.repository.UserRepository;

/**
 * Test para UserService.
 *
 * @author juan.molina
 *
 */
@DataJpaTest
class UserRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    @BeforeEach
    private void addUsersToRepositoryToTest() {
        userRepository.save(new UserEntity(null, new Date(), new Date(), null, "mail.m@mail.com", "12Pasdfg", null, true));
        userRepository.save(new UserEntity(null, new Date(), new Date(), null, "mail@mail.com", "12Pasdfg", null, true));
        userRepository.save(new UserEntity(null, new Date(), new Date(), null, "asdf@test.com", "12Pasdfg", null, true));
    }

    @ParameterizedTest
    @ValueSource(strings = {"mai@mail.com", " ", "", "mail.com", "mail@mailcom"})
    @DisplayName("Test parametrizado de obtener usuario por mail que devuelva nulo")
    void testRepositoryReturnsNullWithBadEmail(String email) {
        assertNull(userRepository.findByEmail(email));
    }

    @ParameterizedTest
    @ValueSource(strings = {"mail.m@mail.com", "mail@mail.com", "asdf@test.com"})
    @DisplayName("Test parametrizado de obtener usuario correctamente")
    void testRepositoryReturnsUser(final String email) {
        assertThat(userRepository.findByEmail(email)).isInstanceOf(UserEntity.class);
    }
}
