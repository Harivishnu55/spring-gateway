package com.gateway.service.impl;

import com.gateway.dto.AuthRequest;
import com.gateway.dto.AuthResponse;
import com.gateway.entity.UserEntity;
import com.gateway.exception.NotFoundException;
import com.gateway.exception.UnAuthorizedRequestException;
import com.gateway.repository.UserRepo;
import com.gateway.service.AuthService;
import com.gateway.util.JwtUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class AuthServiceImpl implements AuthService {

    private static final Logger logger = LoggerFactory.getLogger(AuthServiceImpl.class);

    private final UserRepo userRepo;
    private final JwtUtil jwtUtil;

    public AuthServiceImpl(UserRepo userRepo, JwtUtil jwtUtil){
        this.userRepo=userRepo;
        this.jwtUtil=jwtUtil;
    }

    /**
     * Authenticates the user based on provided credentials and returns a JWT token upon success.
     *
     * @param authRequest the authentication request containing username and password.
     * @return {@link AuthResponse} containing the generated JWT token if authentication is successful.
     *
     * @throws NotFoundException if the username does not exist in the database.
     * @throws UnAuthorizedRequestException if the password is incorrect.
     */
    @Override
    public AuthResponse authenticateUser(AuthRequest authRequest) {

        UserEntity user = userRepo.findByUsername(authRequest.getUsername())
                .orElseThrow(() -> new NotFoundException("User not found"));

        if ( !user.getPassword().equals(authRequest.getPassword())) {
            logger.warn("Invalid login attempt from user : {}",authRequest.getUsername());
            throw new UnAuthorizedRequestException("Invalid password");
        }

        logger.trace("Authentication completed successfully for : {}",authRequest.getUsername());
        return new AuthResponse(jwtUtil.generateToken(authRequest.getUsername()));

    }
}
