package com.bsmm.login.config;

import com.bsmm.login.service.dto.LoginResponse;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;

@Configuration
public class RedisConfig {
    @Bean
    public LettuceConnectionFactory redisConnectionFactory() {
        return new LettuceConnectionFactory("localhost", 6379);
    }

    @Bean
    public RedisTemplate<String, LoginResponse> redisTemplate(LettuceConnectionFactory connectionFactory) {
        RedisTemplate<String, LoginResponse> template = new RedisTemplate<>();
        template.setConnectionFactory(connectionFactory);
        return template;
    }
}