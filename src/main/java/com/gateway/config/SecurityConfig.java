package com.gateway.config;

import com.gateway.filter.JwtAuthenticationFilter;
import com.gateway.filter.RateLimitingFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final JwtAuthenticationFilter jwtAuthenticationFilter;

    private final RateLimitingFilter rateLimitingFilter;

    public SecurityConfig(JwtAuthenticationFilter jwtAuthenticationFilter,RateLimitingFilter rateLimitingFilter){
        this.rateLimitingFilter=rateLimitingFilter;
        this.jwtAuthenticationFilter=jwtAuthenticationFilter;
    }

    /**
     * Configures the Spring Security filter chain for the application.
     *
     * <p>This method defines the security behavior for all endpoints:
     * <ul>
     *   <li>Disables CSRF and form login.</li>
     *   <li>Sets session management to stateless (for JWT usage).</li>
     *   <li>Allows unauthenticated access to the "/auth" endpoint.</li>
     *   <li>Requires authentication for all other requests.</li>
     *   <li>Adds a custom JWT authentication filter before the default username-password filter.</li>
     *   <li>Adds a rate-limiting filter after successful authentication.</li>
     * </ul>
     *
     * @param http the {@link HttpSecurity} object used to configure HTTP security for the application.
     * @return the configured {@link SecurityFilterChain} bean.
     * @throws Exception if an error occurs during configuration.
     */
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .securityMatcher("/**")
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/auth").permitAll()
                        .anyRequest().authenticated()
                )
                .csrf(AbstractHttpConfigurer::disable)
                .sessionManagement(session -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                ).formLogin(AbstractHttpConfigurer::disable);

        http.addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);
        http.addFilterAfter(rateLimitingFilter, JwtAuthenticationFilter.class);

        return http.build();
    }
}

