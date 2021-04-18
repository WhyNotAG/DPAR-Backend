package com.diploma.DPAR.service.dto;

import lombok.Data;

@Data
public class SignInResponseDTO {
    private String accessToken;
    private String refreshToken;
    private Long expiredIn;
    private UserDTO user;
}
