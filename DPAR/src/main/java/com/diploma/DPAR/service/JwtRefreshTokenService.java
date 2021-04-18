package com.diploma.DPAR.service;

import com.diploma.DPAR.domain.JwtRefreshToken;

import java.util.Optional;

public interface JwtRefreshTokenService {

    JwtRefreshToken save(JwtRefreshToken token);

    void delete(JwtRefreshToken token);

    Optional<JwtRefreshToken> findById(String id);

}
