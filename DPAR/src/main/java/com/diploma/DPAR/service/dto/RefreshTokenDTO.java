package com.diploma.DPAR.service.dto;

import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class RefreshTokenDTO {
    @NotBlank
    private String refreshToken;
}
