package com.gateway.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gateway.constants.AppConstants;
import com.gateway.dto.ErrorResponse;
import com.gateway.exception.TokenExpiredException;
import com.gateway.util.JwtUtil;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtUtil jwtUtil;

    public JwtAuthenticationFilter(JwtUtil jwtUtil) {
        this.jwtUtil = jwtUtil;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain)
            throws ServletException, IOException {

        String authHeader = request.getHeader("Authorization");
        String userName = request.getHeader("user-name");

        try {
            if (authHeader != null && authHeader.startsWith("Bearer ")) {
                String token = authHeader.substring(7);

                if (jwtUtil.validateToken(token, userName)) {
                    String username = jwtUtil.extractUsername(token);

                    UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(
                            username, null, List.of()
                    );

                    SecurityContextHolder.getContext().setAuthentication(auth);
                }
            }
            filterChain.doFilter(request, response);
        } catch (TokenExpiredException e) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.setContentType("application/json");
            ErrorResponse errorResponse = ErrorResponse.builder()
                    .success(false)
                    .message(e.getMessage())
                    .statusCode(HttpStatus.UNAUTHORIZED.value())
                    .timeStamp(LocalDateTime.now().format(DateTimeFormatter.ofPattern(AppConstants.RESPONSE_DATE_TIME)))
                    .build();

            ObjectMapper mapper = new ObjectMapper();
            response.getWriter().write(mapper.writeValueAsString(errorResponse));
        }
    }
}

