package com.bsmm.login.service;

import com.bsmm.login.service.dto.LoginRequest;
import com.bsmm.login.service.dto.LoginResponse;
import com.bsmm.login.service.dto.UserDTO;
import com.bsmm.login.service.dto.UserSignup;

import java.util.List;

public interface UserService {
    LoginResponse authenticateUser(LoginRequest loginRequest);

    void logoutUser(String token);

    List<UserDTO> getAll();

    UserDTO create(UserSignup dto);
}
