package com.bsmm.login.service.dto.impl;

import com.bsmm.login.config.JwtUtils;
import com.bsmm.login.service.RedisService;
import com.bsmm.login.service.dto.LoginResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class RedisServiceImpl implements RedisService {
    private final RedisTemplate<String, LoginResponse> redisTemplate;
    private final JwtUtils jwtUtils;

    @Override
    public void saveSession(LoginResponse data) {
        redisTemplate.opsForValue().set(jwtUtils.getClaimId(data.getAccessToken()), data);
    }

    @Override
    public void deleteSession(String token) {
        redisTemplate.delete(jwtUtils.getClaimId(token));
    }

    @Override
    public boolean existSession(String token) {
        LoginResponse response = redisTemplate.opsForValue().get(jwtUtils.getClaimId(token));
        return response != null;
    }
}
