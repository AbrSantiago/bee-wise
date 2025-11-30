package com.beewise.service.impl;

import com.beewise.controller.dto.RefreshTokenResponseDTO;
import com.beewise.exception.InvalidTokenException;
import com.beewise.model.RefreshToken;
import com.beewise.repository.RefreshTokenRepository;
import com.beewise.service.TokenService;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
@Transactional
public class TokenServiceImpl implements TokenService {

    private final JwtService jwtService;
    private final RefreshTokenRepository refreshTokenRepository;

    public TokenServiceImpl(JwtService jwtService, RefreshTokenRepository refreshTokenRepository) {
        this.jwtService = jwtService;
        this.refreshTokenRepository = refreshTokenRepository;
    }

    public RefreshTokenResponseDTO rotateRefreshToken(String oldRefreshToken) {
        RefreshToken token = refreshTokenRepository.findByToken(oldRefreshToken)
                .orElseThrow(() -> new InvalidTokenException("Invalid refresh token"));
        if (token.isRevoked() || token.getExpiresAt().before(new Date())) {
            throw new InvalidTokenException("Refresh token expired or revoked");
        }

        String username = token.getUser().getUsername();
        String newAccessToken = jwtService.generateAccessToken(username);
        String newRefreshToken = jwtService.generateRefreshToken(username);

        RefreshToken newToken = new RefreshToken();
        newToken.setToken(newRefreshToken);
        newToken.setUser(token.getUser());
        newToken.setIssuedAt(new Date());
        newToken.setExpiresAt(new Date(System.currentTimeMillis() + jwtService.getRefreshTokenExpiration()));
        newToken.setRevoked(false);
        refreshTokenRepository.save(newToken);

        token.setRevoked(true);
        refreshTokenRepository.save(token);

        return new RefreshTokenResponseDTO(newAccessToken, newRefreshToken);
    }
}
