package com.gateway.controller;

import org.junit.jupiter.api.Test;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.assertNotNull;

class SecuredControllerTest {

    private final SecuredController controller = new SecuredController();

    @Test
    void testGetData() {
        ResponseEntity<Object> response = controller.getData();
        assertNotNull(response.getBody());
    }

    @Test
    void testGetDataTime() {
        ResponseEntity<Object> response = controller.getDataTime();
        assertNotNull(response.getBody());
    }
}
