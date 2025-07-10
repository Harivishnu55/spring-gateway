package com.gateway.util;

import com.gateway.exception.TokenExpiredException;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;

@Component
public class JwtUtil {

    private static final Logger logger = LoggerFactory.getLogger(JwtUtil.class);

    @Value("${jwt.secret}")
    private String secret;

    @Value("${jwt.expiration}")
    private long expiration;

    private SecretKey secretKey;

    @PostConstruct
    public void init() {
        this.secretKey = Keys.hmacShaKeyFor(secret.getBytes());
    }

    /**
     * Generates a JWT token using the provided username.
     *
     * @param username the username for which the token is to be generated.
     * @return a signed JWT token containing the username as subject and expiration timestamp.
     **/
    public String generateToken(String username) {
        logger.trace("Generating token for : {}",username);
        return Jwts.builder()
                .setSubject(username)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + expiration))
                .signWith(secretKey, SignatureAlgorithm.HS256)
                .compact();
    }

    /**
     * Extracts the username (subject) from the given JWT token.
     *
     * @param token the JWT token from which to extract the username.
     * @return the subject (username) present in the token payload.
     **/
    public String extractUsername(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(secretKey)
                .build()
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
    }

    /**
     * Validates the given JWT token for integrity and expected username.
     *
     * @param token the JWT token to be validated.
     * @param expectedUsername the username that is expected to be in the token.
     * @return true if the token is valid and matches the expected username, false otherwise.
     *
     * @throws TokenExpiredException if the token has expired.
     */
    public boolean validateToken(String token, String expectedUsername) {
        try {
            String actualUsername = extractUsername(token);
            return actualUsername.equals(expectedUsername);
        } catch (ExpiredJwtException e) {
            throw new TokenExpiredException("Token has expired.");
        }
        catch (JwtException e) {
            return false;
        }
    }
}
