package com.gateway.service.impl;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.gateway.dto.AuthRequest;
import com.gateway.dto.AuthResponse;
import com.gateway.entity.UserEntity;
import com.gateway.exception.NotFoundException;
import com.gateway.exception.UnAuthorizedRequestException;
import com.gateway.repository.UserRepo;
import com.gateway.util.JwtUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import java.util.Optional;

class AuthServiceImplTest {

    @Mock
    private UserRepo userRepo;

    @Mock
    private JwtUtil jwtUtil;

    @InjectMocks
    private AuthServiceImpl authService;

    @Captor
    private ArgumentCaptor<String> usernameCaptor;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void success() {
        String username = "testUser";
        String password = "password123";
        AuthRequest request = new AuthRequest(username, password);

        UserEntity userEntity = new UserEntity();
        userEntity.setUsername(username);
        userEntity.setPassword(password);

        when(userRepo.findByUsername(username)).thenReturn(Optional.of(userEntity));
        when(jwtUtil.generateToken(username)).thenReturn("dummy.jwt.token");

        AuthResponse response = authService.authenticateUser(request);

        assertNotNull(response);
        assertEquals("dummy.jwt.token", response.getToken());

        verify(userRepo).findByUsername(usernameCaptor.capture());
        assertEquals(username, usernameCaptor.getValue());

        verify(jwtUtil).generateToken(username);
    }

    @Test
    void userNotFound() {
        String username = "nonexistentUser";
        AuthRequest request = new AuthRequest(username, "anyPassword");

        when(userRepo.findByUsername(username)).thenReturn(Optional.empty());

        NotFoundException ex = assertThrows(NotFoundException.class,
                () -> authService.authenticateUser(request));

        assertEquals("User not found", ex.getMessage());
        verify(userRepo).findByUsername(username);
        verifyNoMoreInteractions(jwtUtil);
    }

    @Test
    void invalidPassword() {
        String username = "testUser";
        String correctPassword = "correctPassword";
        String wrongPassword = "wrongPassword";

        AuthRequest request = new AuthRequest(username, wrongPassword);

        UserEntity userEntity = new UserEntity();
        userEntity.setUsername(username);
        userEntity.setPassword(correctPassword);

        when(userRepo.findByUsername(username)).thenReturn(Optional.of(userEntity));

        UnAuthorizedRequestException ex = assertThrows(UnAuthorizedRequestException.class,
                () -> authService.authenticateUser(request));

        assertEquals("Invalid password", ex.getMessage());
        verify(userRepo).findByUsername(username);
        verifyNoMoreInteractions(jwtUtil);
    }
}

