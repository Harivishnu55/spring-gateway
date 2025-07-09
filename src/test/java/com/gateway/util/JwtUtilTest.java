package com.gateway.util;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;

import static org.junit.jupiter.api.Assertions.*;

class JwtUtilTest {

    private JwtUtil jwtUtil;

    @BeforeEach
    void setUp() {
        jwtUtil = new JwtUtil();
        String secret = "9CKHbZFt9MjSPZzM9MzuZ$2aJns7gYLTbEKw0ZssxvRkM$10$wVDw!";
        ReflectionTestUtils.setField(jwtUtil, "secret", secret);
        long expiration = 1000 * 60 * 5;
        ReflectionTestUtils.setField(jwtUtil, "expiration", expiration);
        jwtUtil.init();
    }

    @Test
    void testGenerateAndValidateToken() {
        String username = "testUser";
        String token = jwtUtil.generateToken(username);

        assertNotNull(token, "Token should not be null");

        String extractedUsername = jwtUtil.extractUsername(token);
        assertEquals(username, extractedUsername, "Extracted username should match");

        boolean isValid = jwtUtil.validateToken(token, username);
        assertTrue(isValid, "Token should be valid for correct username");

        boolean isInvalid = jwtUtil.validateToken(token, "otherUser");
        assertFalse(isInvalid, "Token should be invalid for wrong username");
    }

    @Test
    void testForInvalidToken() {
        String invalidToken = "invalid.jwt.token";

        boolean isValid = jwtUtil.validateToken(invalidToken, "testUser");
        assertFalse(isValid, "Invalid token should not be valid");
    }
}

