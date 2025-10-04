package com.beewise.service.impl;

import com.beewise.controller.dto.RefreshTokenResponseDTO;
import com.beewise.controller.dto.RegisterUserDTO;
import com.beewise.exception.InvalidTokenException;
import com.beewise.model.RefreshToken;
import com.beewise.model.User;
import com.beewise.repository.RefreshTokenRepository;
import com.beewise.service.TokenService;
import com.beewise.service.UserService;
import com.beewise.service.impl.JwtService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
@ActiveProfiles("test")
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
class TokenServiceImplTest {

    @Autowired
    private TokenService tokenService;

    @Autowired
    private RefreshTokenRepository refreshTokenRepository;

    @Autowired
    private UserService userService;

    private User testUser;
    private RefreshToken validRefreshToken;
    private RefreshToken expiredRefreshToken;
    private RefreshToken revokedRefreshToken;

    @BeforeEach
    void setUp() {
        // Crear usuario único para cada test
        testUser = createTestUser("tokenUser_" + UUID.randomUUID().toString().substring(0, 8),
                "token_" + System.currentTimeMillis() + "@test.com");
        validRefreshToken = createValidRefreshToken();
        expiredRefreshToken = createExpiredRefreshToken();
        revokedRefreshToken = createRevokedRefreshToken();
    }

    private User createTestUser(String username, String email) {
        RegisterUserDTO dto = new RegisterUserDTO();
        dto.setUsername(username);
        dto.setEmail(email);
        dto.setName("Token");
        dto.setSurname("User");
        dto.setPassword("password123");
        return userService.registerUser(dto);
    }

    private RefreshToken createValidRefreshToken() {
        String tokenValue = "valid_" + UUID.randomUUID().toString();
        RefreshToken token = new RefreshToken();
        token.setToken(tokenValue);
        token.setUser(testUser);
        token.setIssuedAt(new Date());
        token.setExpiresAt(new Date(System.currentTimeMillis() + 604800000L));
        token.setRevoked(false);
        return refreshTokenRepository.save(token);
    }

    private RefreshToken createExpiredRefreshToken() {
        String tokenValue = "expired_" + UUID.randomUUID().toString();
        RefreshToken token = new RefreshToken();
        token.setToken(tokenValue);
        token.setUser(testUser);
        token.setIssuedAt(new Date(System.currentTimeMillis() - 1000000));
        token.setExpiresAt(new Date(System.currentTimeMillis() - 1000));
        token.setRevoked(false);
        return refreshTokenRepository.save(token);
    }

    private RefreshToken createRevokedRefreshToken() {
        String tokenValue = "revoked_" + UUID.randomUUID().toString();
        RefreshToken token = new RefreshToken();
        token.setToken(tokenValue);
        token.setUser(testUser);
        token.setIssuedAt(new Date());
        token.setExpiresAt(new Date(System.currentTimeMillis() + 604800000L));
        token.setRevoked(true);
        return refreshTokenRepository.save(token);
    }

    @Test
    void rotateRefreshToken_validToken_returnsNewTokens() {
        String oldToken = validRefreshToken.getToken();
        long tokenCountBefore = refreshTokenRepository.count();

        RefreshTokenResponseDTO result = tokenService.rotateRefreshToken(oldToken);

        assertNotNull(result);
        assertNotNull(result.getAccessTokenToken());
        assertNotNull(result.getRefreshToken());
        assertNotEquals(oldToken, result.getRefreshToken());

        long tokenCountAfter = refreshTokenRepository.count();
        assertEquals(tokenCountBefore + 1, tokenCountAfter);
    }

    @Test
    void rotateRefreshToken_invalidToken_throwsException() {
        String invalidToken = "invalid_token_" + UUID.randomUUID().toString();

        InvalidTokenException exception = assertThrows(
                InvalidTokenException.class,
                () -> tokenService.rotateRefreshToken(invalidToken)
        );

        assertEquals("Invalid refresh token", exception.getMessage());
    }

    @Test
    void rotateRefreshToken_expiredToken_throwsException() {
        String expiredToken = expiredRefreshToken.getToken();

        InvalidTokenException exception = assertThrows(
                InvalidTokenException.class,
                () -> tokenService.rotateRefreshToken(expiredToken)
        );

        assertEquals("Refresh token expired or revoked", exception.getMessage());
    }

    @Test
    void rotateRefreshToken_revokedToken_throwsException() {
        String revokedToken = revokedRefreshToken.getToken();

        InvalidTokenException exception = assertThrows(
                InvalidTokenException.class,
                () -> tokenService.rotateRefreshToken(revokedToken)
        );

        assertEquals("Refresh token expired or revoked", exception.getMessage());
    }

    @Test
    void contextLoads() {
        assertNotNull(tokenService);
        assertNotNull(refreshTokenRepository);
        assertNotNull(userService);
    }
}