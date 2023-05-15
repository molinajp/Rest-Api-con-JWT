package com.ascend.individual.juanpablomolina.dto;

import java.util.Date;
import java.util.Set;
import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserDtoResponse {
    
    private UUID id;
    
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "MMM dd, yyyy HH:mm:ss a", timezone = "GMT-3")
    private Date created;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "MMM dd, yyyy HH:mm:ss a", timezone = "GMT-3")
    private Date lastLogin;

    private String name;

    private String email;

    private String password;

    private Set<PhoneDtoResponse> phones;

    /**
     * Token JWT de autenticación del usuario. Este campo no es persistido en la
     * base de datos.
     */
    private String token;

    private boolean isActive;

}
