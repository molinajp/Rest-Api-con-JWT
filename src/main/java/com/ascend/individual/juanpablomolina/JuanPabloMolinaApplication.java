package com.ascend.individual.juanpablomolina;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * REST API creada como parte del curso Ascend. Proyecto Individual de Juan
 * Pablo Molina
 * 
 * <p>Este proyecto tiene funcionalidades básicas como registrarse y loguearse,
 * seguridad con JWT, refresco de token tests unitarios y swagger
 *
 * @author juan.molina
 */

@SpringBootApplication
public class JuanPabloMolinaApplication {

    public static void main(final String[] args) {
        SpringApplication.run(JuanPabloMolinaApplication.class, args);
    }

    @Bean
    PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

}
