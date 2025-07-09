package com.gateway.filter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import java.util.concurrent.TimeUnit;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class RateLimitingFilterTest {

    private static final Logger log = LoggerFactory.getLogger(RateLimitingFilterTest.class);
    @Mock
    private StringRedisTemplate redisTemplate;

    @Mock
    private ZSetOperations<String, String> zSetOperations;

    @Mock
    private HttpServletRequest request;

    @Mock
    private HttpServletResponse response;

    @Mock
    private FilterChain filterChain;

    @InjectMocks
    private RateLimitingFilter rateLimitingFilter;

    @BeforeEach
    void setUp() {
        rateLimitingFilter = new RateLimitingFilter(redisTemplate);
        setPrivateField(rateLimitingFilter, "limit", 10);
        setPrivateField(rateLimitingFilter, "windowSeconds", 60);

        SecurityContextHolder.getContext().setAuthentication(
                new UsernamePasswordAuthenticationToken(
                        "test-user",
                        null,
                        List.of(new SimpleGrantedAuthority("ROLE_USER"))
                )
        );

    }

    @Test
    void shouldAllowRequest_WhenUnderLimit() throws ServletException, IOException {
        when(zSetOperations.zCard("rate:sliding:test-user")).thenReturn(5L);
        when(redisTemplate.opsForZSet()).thenReturn(zSetOperations);

        rateLimitingFilter.doFilterInternal(request, response, filterChain);

        verify(zSetOperations).removeRangeByScore(eq("rate:sliding:test-user"), anyDouble(), anyDouble());
        verify(zSetOperations).add(eq("rate:sliding:test-user"), anyString(), anyDouble());
        verify(redisTemplate).expire(eq("rate:sliding:test-user"), anyLong(), eq(TimeUnit.SECONDS));
        verify(filterChain).doFilter(request, response);
    }

    @Test
    void shouldBlockRequest_WhenOverLimit() throws Exception {
        when(zSetOperations.zCard("rate:sliding:test-user")).thenReturn(11L);
        when(redisTemplate.opsForZSet()).thenReturn(zSetOperations);

        PrintWriter writer = mock(PrintWriter.class);
        when(response.getWriter()).thenReturn(writer);

        rateLimitingFilter.doFilterInternal(request, response, filterChain);

        verify(response).setStatus(429);
        verify(writer).write("Rate limit exceeded. Try again later.");
        verify(filterChain, never()).doFilter(request, response);
    }

    @Test
    void shouldSkipRateLimiting_WhenUnauthenticated() throws ServletException, IOException {
        SecurityContextHolder.clearContext();

        rateLimitingFilter.doFilterInternal(request, response, filterChain);

        verify(filterChain).doFilter(request, response);
        verifyNoInteractions(redisTemplate);
    }

    @Test
    void shouldHandleConcurrentRequestsWithoutError() throws InterruptedException, ServletException, IOException {

        int threadCount = 1000;
        Thread[] threads = new Thread[threadCount];

        for (int i = 0; i < threadCount; i++) {
            int finalI = i;
            threads[i] = new Thread(() -> {
                try {
                    log.info("Thread {} is executing", finalI);
                    rateLimitingFilter.doFilterInternal(request, response, filterChain);
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            });
            threads[i].start();
        }

        for (Thread t : threads) {
            t.join();
        }

        verify(filterChain, times(threadCount)).doFilter(request, response);
    }


    private void setPrivateField(Object target, String fieldName, Object value) {
        try {
            var field = target.getClass().getDeclaredField(fieldName);
            field.setAccessible(true);
            field.set(target, value);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}



