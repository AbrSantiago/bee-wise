package com.beewise.model;

import org.junit.jupiter.api.Test;

import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;

class RefreshTokenTest {

    @Test
    void constructor_createsEmptyObject() {
        RefreshToken refreshToken = new RefreshToken();

        assertNull(refreshToken.getId());
        assertNull(refreshToken.getUser());
        assertNull(refreshToken.getToken());
        assertNull(refreshToken.getIssuedAt());
        assertNull(refreshToken.getExpiresAt());
        assertFalse(refreshToken.isRevoked());
    }

    @Test
    void setId_setsValueCorrectly() {
        RefreshToken refreshToken = new RefreshToken();
        Long id = 1L;

        refreshToken.setId(id);

        assertEquals(id, refreshToken.getId());
    }

    @Test
    void setUser_setsValueCorrectly() {
        RefreshToken refreshToken = new RefreshToken();
        User user = new User();
        user.setId(10L);

        refreshToken.setUser(user);

        assertEquals(user, refreshToken.getUser());
        assertEquals(10L, refreshToken.getUser().getId());
    }

    @Test
    void setToken_setsValueCorrectly() {
        RefreshToken refreshToken = new RefreshToken();
        String token = "abc123def456";

        refreshToken.setToken(token);

        assertEquals(token, refreshToken.getToken());
    }

    @Test
    void setIssuedAt_setsValueCorrectly() {
        RefreshToken refreshToken = new RefreshToken();
        Date issuedAt = new Date();

        refreshToken.setIssuedAt(issuedAt);

        assertEquals(issuedAt, refreshToken.getIssuedAt());
    }

    @Test
    void setExpiresAt_setsValueCorrectly() {
        RefreshToken refreshToken = new RefreshToken();
        Date expiresAt = new Date();

        refreshToken.setExpiresAt(expiresAt);

        assertEquals(expiresAt, refreshToken.getExpiresAt());
    }

    @Test
    void setRevoked_setsValueCorrectly() {
        RefreshToken refreshToken = new RefreshToken();

        refreshToken.setRevoked(true);
        assertTrue(refreshToken.isRevoked());

        refreshToken.setRevoked(false);
        assertFalse(refreshToken.isRevoked());
    }

    @Test
    void defaultRevoked_isFalse() {
        RefreshToken refreshToken = new RefreshToken();

        assertFalse(refreshToken.isRevoked());
    }

    @Test
    void allGetters_workCorrectly() {
        RefreshToken refreshToken = new RefreshToken();
        Long id = 5L;
        User user = new User();
        user.setId(20L);
        String token = "testtoken123";
        Date issuedAt = new Date(System.currentTimeMillis() - 10000);
        Date expiresAt = new Date(System.currentTimeMillis() + 3600000);

        refreshToken.setId(id);
        refreshToken.setUser(user);
        refreshToken.setToken(token);
        refreshToken.setIssuedAt(issuedAt);
        refreshToken.setExpiresAt(expiresAt);
        refreshToken.setRevoked(true);

        assertEquals(id, refreshToken.getId());
        assertEquals(user, refreshToken.getUser());
        assertEquals(token, refreshToken.getToken());
        assertEquals(issuedAt, refreshToken.getIssuedAt());
        assertEquals(expiresAt, refreshToken.getExpiresAt());
        assertTrue(refreshToken.isRevoked());
    }

    @Test
    void setUser_withNullValue_handlesCorrectly() {
        RefreshToken refreshToken = new RefreshToken();

        refreshToken.setUser(null);

        assertNull(refreshToken.getUser());
    }

    @Test
    void setToken_withNullValue_handlesCorrectly() {
        RefreshToken refreshToken = new RefreshToken();

        refreshToken.setToken(null);

        assertNull(refreshToken.getToken());
    }

    @Test
    void setToken_withEmptyString_handlesCorrectly() {
        RefreshToken refreshToken = new RefreshToken();

        refreshToken.setToken("");

        assertEquals("", refreshToken.getToken());
    }

    @Test
    void setDates_withNullValues_handlesCorrectly() {
        RefreshToken refreshToken = new RefreshToken();

        refreshToken.setIssuedAt(null);
        refreshToken.setExpiresAt(null);

        assertNull(refreshToken.getIssuedAt());
        assertNull(refreshToken.getExpiresAt());
    }

    @Test
    void fullObjectCreation_worksCorrectly() {
        User user = new User();
        user.setId(15L);
        user.setUsername("testuser");

        Date now = new Date();
        Date expiry = new Date(now.getTime() + 86400000);

        RefreshToken refreshToken = new RefreshToken();
        refreshToken.setId(100L);
        refreshToken.setUser(user);
        refreshToken.setToken("very-long-secure-token-string-here");
        refreshToken.setIssuedAt(now);
        refreshToken.setExpiresAt(expiry);
        refreshToken.setRevoked(false);

        assertEquals(100L, refreshToken.getId());
        assertEquals(user, refreshToken.getUser());
        assertEquals("very-long-secure-token-string-here", refreshToken.getToken());
        assertEquals(now, refreshToken.getIssuedAt());
        assertEquals(expiry, refreshToken.getExpiresAt());
        assertFalse(refreshToken.isRevoked());
    }
}