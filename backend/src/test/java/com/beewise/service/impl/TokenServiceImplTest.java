package com.beewise.service.impl;

import com.beewise.controller.dto.NewShopItemDTO;
import com.beewise.controller.dto.RefreshTokenResponseDTO;
import com.beewise.controller.dto.RegisterUserDTO;
import com.beewise.exception.InvalidTokenException;
import com.beewise.model.ItemCategory;
import com.beewise.model.Level;
import com.beewise.model.RefreshToken;
import com.beewise.model.User;
import com.beewise.repository.LevelRepository;
import com.beewise.repository.RefreshTokenRepository;
import com.beewise.service.ShopService;
import com.beewise.service.TokenService;
import com.beewise.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
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

    @Autowired
    private ShopService shopService;

    @Autowired
    private LevelRepository levelRepository;

    private User testUser;
    private RefreshToken validRefreshToken;
    private RefreshToken expiredRefreshToken;
    private RefreshToken revokedRefreshToken;

    @BeforeEach
    void setUp() {

        createDefaultLevels();

        createItem("Default skin", ItemCategory.SKIN);
        createItem("Default hair", ItemCategory.HAIR);
        createItem("Default shirt", ItemCategory.SHIRT);
        createItem("Default background", ItemCategory.BACKGROUND);
        testUser = createTestUser("tokenUser_" + UUID.randomUUID().toString().substring(0, 8),
                "token_" + System.currentTimeMillis() + "@test.com");
        validRefreshToken = createValidRefreshToken();
        expiredRefreshToken = createExpiredRefreshToken();
        revokedRefreshToken = createRevokedRefreshToken();
    }

    private void createItem(String name, ItemCategory category) {
        NewShopItemDTO item = new NewShopItemDTO();
        item.setName(name);
        item.setCategory(category);
        item.setImage("");
        item.setPrice(0);
        shopService.createShopItem(item);
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
        String tokenValue = "valid_" + UUID.randomUUID();
        RefreshToken token = new RefreshToken();
        token.setToken(tokenValue);
        token.setUser(testUser);
        token.setIssuedAt(new Date());
        token.setExpiresAt(new Date(System.currentTimeMillis() + 604800000L));
        token.setRevoked(false);
        return refreshTokenRepository.save(token);
    }

    private RefreshToken createExpiredRefreshToken() {
        String tokenValue = "expired_" + UUID.randomUUID();
        RefreshToken token = new RefreshToken();
        token.setToken(tokenValue);
        token.setUser(testUser);
        token.setIssuedAt(new Date(System.currentTimeMillis() - 1000000));
        token.setExpiresAt(new Date(System.currentTimeMillis() - 1000));
        token.setRevoked(false);
        return refreshTokenRepository.save(token);
    }

    private RefreshToken createRevokedRefreshToken() {
        String tokenValue = "revoked_" + UUID.randomUUID();
        RefreshToken token = new RefreshToken();
        token.setToken(tokenValue);
        token.setUser(testUser);
        token.setIssuedAt(new Date());
        token.setExpiresAt(new Date(System.currentTimeMillis() + 604800000L));
        token.setRevoked(true);
        return refreshTokenRepository.save(token);
    }

    private void createDefaultLevels() {
        if (levelRepository.count() == 0) {
            Level level1 = new Level();
            level1.setName("Beginner");
            level1.setMinPoints(0);
            level1.setIconUrl("icon1.png");
            levelRepository.save(level1);

            Level level2 = new Level();
            level2.setName("Intermediate");
            level2.setMinPoints(100);
            level2.setIconUrl("icon2.png");
            levelRepository.save(level2);
        }
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
        String invalidToken = "invalid_token_" + UUID.randomUUID();

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