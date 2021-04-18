package com.diploma.DPAR.service.Impl;

import com.diploma.DPAR.domain.JwtRefreshToken;
import com.diploma.DPAR.domain.User;
import com.diploma.DPAR.repo.JwtRefreshTokenRepo;
import com.diploma.DPAR.service.JwtRefreshTokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class JwtRefreshTokenServiceImpl implements JwtRefreshTokenService {

    private JwtRefreshTokenRepo jwtRefreshTokenRepository;

    @Autowired
    public JwtRefreshTokenServiceImpl(JwtRefreshTokenRepo jwtRefreshTokenRepository) {
        this.jwtRefreshTokenRepository = jwtRefreshTokenRepository;
    }

    @Override
    public JwtRefreshToken save(JwtRefreshToken token) {
        if (token == null) {
            return null;
        }

        clearTokenForUser(token.getUser());

        return jwtRefreshTokenRepository.save(token);
    }

    /*
     * Delete all users refresh tokens if count great or equal ten
     */
    private void clearTokenForUser(User user) {
        List<JwtRefreshToken> tokens = jwtRefreshTokenRepository.findAllByUserId(user.getId());
        if (tokens.size() >= 10) {
            for (JwtRefreshToken token:
                    tokens) {
                delete(token);
            }
        }
    }

    @Override
    public void delete(JwtRefreshToken token) {
        if (token == null) { return; }

        jwtRefreshTokenRepository.delete(token);
    }

    @Override
    public Optional<JwtRefreshToken> findById(String id) {
        return jwtRefreshTokenRepository.findById(id);
    }
}
