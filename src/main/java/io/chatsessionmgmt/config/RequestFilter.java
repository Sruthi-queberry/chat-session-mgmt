package io.chatsessionmgmt.config;

import io.chatsessionmgmt.exception.RateLimitExceededException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.UUID;

@Slf4j
@Component
public class RequestFilter extends OncePerRequestFilter {

    private static final String API_KEY_HEADER = "X-API-KEY";
    private final String apiKey;
    private final RedisRateLimiter rateLimiter;

    public RequestFilter(RedisRateLimiter rateLimiter) {
//        this.apiKey = System.getenv("API_KEY");  // to test with .env from docker
        this.apiKey = "testKey";
        if (this.apiKey == null) {
            throw new IllegalStateException("API_KEY environment variable not set!");
        }
        this.rateLimiter = rateLimiter;
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        String path = request.getRequestURI();

        return path.startsWith("/swagger-ui")
                || path.startsWith("/v3/api-docs")
                || path.equals("/swagger-ui.html")
                || path.startsWith("/actuator");
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {

        String requestApiKey = request.getHeader(API_KEY_HEADER);
        long startTime = System.currentTimeMillis();
        String requestId = UUID.randomUUID().toString();

        log.info("Request id {} method: {} URI: {} remote address: {}",requestId ,request.getMethod(), request.getRequestURI(), request.getRemoteAddr());

        if (requestApiKey == null || !requestApiKey.equals(apiKey)) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.getWriter().write("Unauthorized: Invalid API Key");
            log.info("Unauthorized: Invalid API Key");
            return;
        }

        if (!rateLimiter.allowRequest(requestApiKey)) {
            log.warn("Too many requests - Rate limit exceeded");
            throw new RateLimitExceededException(
                    "Too many requests. Please try again later."
            );
        }

        filterChain.doFilter(request, response);

        long duration = System.currentTimeMillis() - startTime;
        log.info("Response id {} status: {} {} ({} ms)", requestId, response.getStatus(), response.getContentType(), duration);
    }
}

