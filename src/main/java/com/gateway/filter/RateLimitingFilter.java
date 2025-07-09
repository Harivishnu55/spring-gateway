package com.gateway.filter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.time.Instant;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

@Component
public class RateLimitingFilter extends OncePerRequestFilter
{

    private final StringRedisTemplate redisTemplate;

    @Value("${request.limit}")
    private int limit;

    @Value("${request.window.seconds}")
    private int windowSeconds;

    public RateLimitingFilter(StringRedisTemplate redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain)
            throws ServletException, IOException {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication != null && authentication.isAuthenticated()) {
            String username = authentication.getName();
            String key = "rate:sliding:" + username;

            long now = Instant.now().getEpochSecond();
            long windowStart = now - windowSeconds;

            ZSetOperations<String, String> zSetOps = redisTemplate.opsForZSet();

            zSetOps.removeRangeByScore(key, 0, windowStart);

            Long currentCount = zSetOps.zCard(key);

            if (currentCount != null && currentCount >= limit) {
                response.setStatus(HttpStatus.TOO_MANY_REQUESTS.value());
                response.getWriter().write("Rate limit exceeded. Try again later.");
                return;
            }

            zSetOps.add(key, UUID.randomUUID().toString(), now);
            redisTemplate.expire(key,  windowSeconds + 1L, TimeUnit.SECONDS);
        }


        filterChain.doFilter(request, response);
    }
}

