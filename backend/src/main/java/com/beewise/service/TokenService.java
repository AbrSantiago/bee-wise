package com.beewise.service;

import com.beewise.controller.dto.RefreshTokenResponseDTO;

public interface TokenService {
    RefreshTokenResponseDTO rotateRefreshToken(String oldRefreshToken);
}
