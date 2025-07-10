package com.gateway.controller;

import com.gateway.dto.AuthRequest;
import com.gateway.dto.AuthResponse;
import com.gateway.service.AuthService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private static final Logger logger = LoggerFactory.getLogger(AuthController.class);

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    /**
     * Authenticates the user using the provided credentials and returns a JWT token on success.
     *
     * @param request the {@link AuthRequest} object containing username and password.
     * @return {@link ResponseEntity} containing the {@link AuthResponse} with the JWT token.
     *
     * <p><b>Usage:</b> Call this endpoint with valid credentials to obtain a JWT token.</p>
     * <p><b>URL:</b> POST /auth</p>
     * <p><b>Returns:</b> 200 OK with JWT if credentials are valid, otherwise 401/404 depending on failure type.</p>
     */
    @PostMapping
    public ResponseEntity<AuthResponse> login(@Valid @RequestBody AuthRequest request) {
        logger.trace("Received login request from : {}",request.getUsername());
        return ResponseEntity.status(HttpStatus.OK).body(authService.authenticateUser(request));
    }
}


