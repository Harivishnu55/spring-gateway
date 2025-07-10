package com.gateway.exception;

import com.gateway.dto.ErrorResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.ResponseEntity;


import static org.junit.jupiter.api.Assertions.*;

class GlobalExceptionHandlerTest {

    private GlobalExceptionHandler handler;

    @BeforeEach
    void setUp() {
        handler = new GlobalExceptionHandler();
    }

    @Test
    void testHandleNotFoundException() {
        NotFoundException ex = new NotFoundException("User not found");

        ResponseEntity<ErrorResponse> response = handler.handleUserNotFound(ex);

        assertEquals(404, response.getStatusCode().value());
        assertFalse(response.getBody().isSuccess());
        assertEquals(404, response.getBody().getStatusCode());
        assertNotNull(response.getBody().getTimeStamp());
    }

    @Test
    void testHandleUnAuthorizedRequestException() {
        UnAuthorizedRequestException ex = new UnAuthorizedRequestException("Invalid credentials");

        ResponseEntity<ErrorResponse> response = handler.handleUnAuthorizedRequest(ex);

        assertEquals(401, response.getStatusCode().value());
        assertFalse(response.getBody().isSuccess());
        assertEquals(401, response.getBody().getStatusCode());

    }
}
