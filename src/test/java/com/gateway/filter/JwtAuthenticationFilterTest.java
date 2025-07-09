package com.gateway.filter;

import com.gateway.util.JwtUtil;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.security.core.context.SecurityContextHolder;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class JwtAuthenticationFilterTest {

    @Mock
    private JwtUtil jwtUtil;

    @InjectMocks
    private JwtAuthenticationFilter jwtFilter;

    @Mock
    private HttpServletRequest request;

    @Mock
    private HttpServletResponse response;

    @Mock
    private FilterChain filterChain;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
        SecurityContextHolder.clearContext(); // clear context before every test
    }

    @Test
    void shouldAuthenticateWhenTokenIsValid() throws ServletException, IOException {
        String token = "valid.token.here";
        String userName = "testUser";

        when(request.getHeader("Authorization")).thenReturn("Bearer " + token);
        when(request.getHeader("user-name")).thenReturn(userName);
        when(jwtUtil.validateToken(token, userName)).thenReturn(true);
        when(jwtUtil.extractUsername(token)).thenReturn(userName);

        jwtFilter.doFilterInternal(request, response, filterChain);

        assertNotNull(SecurityContextHolder.getContext().getAuthentication());
        assertEquals(userName, SecurityContextHolder.getContext().getAuthentication().getPrincipal());

        verify(filterChain).doFilter(request, response);
    }

    @Test
    void skipAuthenticationWhenNoAuthorizationHeader() throws Exception {
        when(request.getHeader("Authorization")).thenReturn(null);

        jwtFilter.doFilterInternal(request, response, filterChain);

        assertNull(SecurityContextHolder.getContext().getAuthentication());
        verify(filterChain).doFilter(request, response);
    }

    @Test
    void skipAuthenticationWhenAuthorizationHeaderIsInvalid() throws Exception {
        when(request.getHeader("Authorization")).thenReturn("InvalidHeader");

        jwtFilter.doFilterInternal(request, response, filterChain);

        assertNull(SecurityContextHolder.getContext().getAuthentication());
        verify(filterChain).doFilter(request, response);
    }

    @Test
    void shouldNotSetAuthenticationIfTokenIsInvalid() throws Exception {
        String token = "invalid.token";
        String userName = "user";

        when(request.getHeader("Authorization")).thenReturn("Bearer " + token);
        when(request.getHeader("user-name")).thenReturn(userName);
        when(jwtUtil.validateToken(token, userName)).thenReturn(false);

        jwtFilter.doFilterInternal(request, response, filterChain);

        assertNull(SecurityContextHolder.getContext().getAuthentication());
        verify(filterChain).doFilter(request, response);
    }
}
