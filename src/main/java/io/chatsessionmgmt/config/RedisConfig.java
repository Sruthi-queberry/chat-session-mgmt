package io.chatsessionmgmt.config;

import io.github.cdimascio.dotenv.Dotenv;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.StringRedisTemplate;

@Slf4j
@Configuration
public class RedisConfig {
    Dotenv dotenv = Dotenv.load();
    @Bean
    public RedisConnectionFactory redisConnectionFactory() {
        String host =  dotenv.get("REDIS_HOST");
        int port = Integer.parseInt(dotenv.get("REDIS_PORT"));
        log.info("Redis host: {} port: {}", host, port);
        return new LettuceConnectionFactory(host, port);
    }

    @Bean
    public StringRedisTemplate redisTemplate(RedisConnectionFactory connectionFactory) {
        return new StringRedisTemplate(connectionFactory);
    }
}

