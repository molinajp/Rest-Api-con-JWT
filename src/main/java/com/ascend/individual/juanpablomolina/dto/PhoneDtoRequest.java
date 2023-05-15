package com.ascend.individual.juanpablomolina.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PhoneDtoRequest {

    private long number;

    private int cityCode;

    private String countryCode;
    
}
