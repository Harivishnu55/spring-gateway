package com.gateway.service;

import com.gateway.dto.AuthRequest;
import com.gateway.dto.AuthResponse;

public interface AuthService {

    public AuthResponse authenticateUser(AuthRequest authRequest);
}
