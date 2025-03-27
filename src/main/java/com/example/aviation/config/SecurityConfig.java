package com.example.aviation.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.Getter;

import org.springframework.lang.NonNull;

import java.util.Arrays;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

@Configuration
public class SecurityConfig implements WebMvcConfigurer {

    private final Map<String, Map<String, RateLimiter>> rateLimiters = new ConcurrentHashMap<>();

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http
                .csrf(csrf -> csrf.disable())
                .cors(cors -> cors.configurationSource(corsConfigurationSource()))
                .build();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(Arrays.asList("http://localhost:3001","http://localhost:3000"));
        configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "PATCH", "DELETE", "OPTIONS"));
        configuration.setAllowedHeaders(Arrays.asList("authorization", "content-type", "x-auth-token"));
        configuration.setExposedHeaders(Arrays.asList("x-auth-token"));
        configuration.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }

    @Override
    public void addInterceptors(@NonNull InterceptorRegistry registry) {
        registry.addInterceptor(rateLimitingInterceptor());
    }

    @Bean
    public HandlerInterceptor rateLimitingInterceptor() {
        return new HandlerInterceptor() {
            @Override
            public boolean preHandle(@NonNull HttpServletRequest request, @NonNull HttpServletResponse response, @NonNull Object handler) throws Exception {
                String clientIp = request.getRemoteAddr();
                String endpoint = request.getRequestURI();

                rateLimiters.putIfAbsent(endpoint, new ConcurrentHashMap<>());
                Map<String, RateLimiter> endpointRateLimiters = rateLimiters.get(endpoint);

                endpointRateLimiters.putIfAbsent(clientIp, new RateLimiter(10, TimeUnit.MINUTES));
                RateLimiter rateLimiter = endpointRateLimiters.get(clientIp);

                if (!rateLimiter.tryAcquire()) {
                    response.setStatus(HttpStatus.TOO_MANY_REQUESTS.value());
                    long now = System.currentTimeMillis();
                    // https://developer.mozilla.org/en-US/docs/Web/HTTP/Reference/Headers/Retry-After
                    long retryAfter = rateLimiter.getWindowStart() + rateLimiter.getTimeWindowMillis() - now;
                    response.addHeader("Retry-After", String.valueOf(retryAfter/1000));
                    return false;
                }

                return true;
            }
        };
    }

    @Getter
    private static class RateLimiter {
        private final long maxRequests;
        private final long timeWindowMillis;
        private long requests;
        private long windowStart;

        public RateLimiter(long maxRequests, TimeUnit timeWindow) {
            this.maxRequests = maxRequests;
            this.timeWindowMillis = timeWindow.toMillis(1);
            this.requests = 0;
            this.windowStart = System.currentTimeMillis();
        }

        public synchronized boolean tryAcquire() {
            long now = System.currentTimeMillis();
            if (now - windowStart > timeWindowMillis) {
                windowStart = now;
                requests = 0;
            }

            if (requests < maxRequests) {
                requests++;
                return true;
            }

            return false;
        }
    }
}
