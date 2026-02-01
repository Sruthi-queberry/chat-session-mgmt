package io.chatsessionmgmt.config;

import io.chatsessionmgmt.exception.RateLimitExceededException;
import io.chatsessionmgmt.exception.UnauthorizedException;
import io.chatsessionmgmt.service.ApiKeyService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Collections;
import java.util.UUID;

@Slf4j
@Component
public class RequestFilter extends OncePerRequestFilter {

    private static final String API_KEY_HEADER = "X-API-KEY";
    private final RedisRateLimiter rateLimiter;
    private final ApiKeyService apiKeyService;


    public RequestFilter(RedisRateLimiter rateLimiter, ApiKeyService apiKeyService) {
        this.apiKeyService = apiKeyService;
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

        String userId = apiKeyService.resolveUser(requestApiKey);

        if (userId == null) {
            throw new UnauthorizedException("Invalid API key");
        }

        long startTime = System.currentTimeMillis();
        String requestId = UUID.randomUUID().toString();

        log.info("Request id {} method: {} URI: {} remote address: {}",requestId ,request.getMethod(), request.getRequestURI(), request.getRemoteAddr());

        if (!rateLimiter.allowRequest(requestApiKey)) {
            log.warn("Too many requests - Rate limit exceeded");
            throw new RateLimitExceededException(
                    "Too many requests. Please try again later."
            );
        }

        UserContext.set(userId);
        try {
            filterChain.doFilter(request, response);
        }finally {
            UserContext.clear();
        }
        long duration = System.currentTimeMillis() - startTime;
        log.info("Response id {} status: {} {} ({} ms)", requestId, response.getStatus(), response.getContentType(), duration);
    }
}

