package com.bsmm.login.service;

import com.bsmm.login.service.dto.LoginResponse;
import org.springframework.stereotype.Service;

@Service
public interface RedisService {
    void saveSession(LoginResponse data);

    void deleteSession(String key);

    boolean existSession(String token);
}
