package com.gateway.config;

import com.gateway.filter.JwtAuthenticationFilter;
import com.gateway.filter.RateLimitingFilter;
import org.junit.jupiter.api.Test;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;

class SecurityConfigTest {

    @Test
    void testSecurityFilterChain() throws Exception {
        JwtAuthenticationFilter jwtAuthenticationFilter = mock(JwtAuthenticationFilter.class);
        RateLimitingFilter rateLimitingFilter = mock(RateLimitingFilter.class);

        SecurityConfig config = new SecurityConfig(jwtAuthenticationFilter, rateLimitingFilter);

        HttpSecurity http = mock(HttpSecurity.class, RETURNS_DEEP_STUBS);

        when(http.securityMatcher(anyString())).thenReturn(http);
        when(http.authorizeHttpRequests(any())).thenReturn(http);
        when(http.sessionManagement(any())).thenReturn(http);
        when(http.addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)).thenReturn(http);
        when(http.addFilterAfter(rateLimitingFilter, JwtAuthenticationFilter.class)).thenReturn(http);

        SecurityFilterChain chain = config.securityFilterChain(http);

        assertNotNull(chain);

        verify(http).addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);
        verify(http).addFilterAfter(rateLimitingFilter, JwtAuthenticationFilter.class);
    }
}

