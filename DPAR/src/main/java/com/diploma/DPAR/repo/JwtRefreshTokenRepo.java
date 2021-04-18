package com.diploma.DPAR.repo;

import com.diploma.DPAR.domain.JwtRefreshToken;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface JwtRefreshTokenRepo extends JpaRepository<JwtRefreshToken, String> {

    List<JwtRefreshToken> findAllByUserId(Long userId);
}
