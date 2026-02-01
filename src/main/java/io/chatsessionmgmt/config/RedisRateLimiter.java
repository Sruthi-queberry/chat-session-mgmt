package io.chatsessionmgmt.config;

import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import java.time.Duration;

@Component
public class RedisRateLimiter {

    private final StringRedisTemplate redisTemplate;
    private final int MAX_REQUESTS = 5; // 5 requests
    private final Duration WINDOW = Duration.ofMinutes(1);

    public RedisRateLimiter(StringRedisTemplate redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    public boolean allowRequest(String clientId) {
        String key = "rate:" + clientId;
        Long current = redisTemplate.opsForValue().increment(key);
        if (current == 1) {
            redisTemplate.expire(key, WINDOW); // set TTL on first request
        }

        return current <= MAX_REQUESTS;
    }
}

