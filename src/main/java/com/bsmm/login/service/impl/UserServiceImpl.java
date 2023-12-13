package com.bsmm.login.service.impl;

import com.bsmm.login.config.JwtUtils;
import com.bsmm.login.models.User;
import com.bsmm.login.repository.UserRepository;
import com.bsmm.login.service.UserService;
import com.bsmm.login.service.dto.LoginRequest;
import com.bsmm.login.service.dto.LoginResponse;
import com.bsmm.login.service.dto.UserDTO;
import com.bsmm.login.service.dto.UserSignup;
import com.bsmm.login.service.dto.impl.UserDetailsImpl;
import com.bsmm.login.service.mapper.UserMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder encoder;
    private final AuthenticationManager authenticationManager;
    private final UserDetailsServiceImpl userDetailsService;
    private final JwtUtils jwtUtils;

    @Override
    public LoginResponse authenticateUser(LoginRequest loginRequest) {
        Authentication authentication = authenticationManager
                .authenticate(new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));
        SecurityContextHolder.getContext().setAuthentication(authentication);
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        return jwtUtils.getResponse(userDetails);
    }

    @Override
    public LoginResponse refreshToken(String token) {
        jwtUtils.validateJwtToken(token);
        UserDetailsImpl userDetails = userDetailsService.loadUserByUsername(jwtUtils.getUserNameFromJwtToken(token));
        return jwtUtils.getResponse(userDetails);
    }

    @Override
    public void logoutUser(String token) {
        log.info("Eliminar session desde REDIS");
    }

    @Override
    public List<UserDTO> getAll() {
        return userRepository.findAll().stream().map(UserMapper.INSTANCE::toDto).toList();
    }

    @Override
    public UserDTO create(UserSignup dto) {
        if (Boolean.TRUE.equals(userRepository.existsByUsername(dto.getUsername()))) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Error: Username is already taken!");
        }

        if (Boolean.TRUE.equals(userRepository.existsByEmail(dto.getEmail()))) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Error: Email is already in use!");
        }

        User user = UserMapper.INSTANCE.toEntity(dto);
        user.setPassword(encoder.encode(dto.getPassword()));

        return UserMapper.INSTANCE.toDto(userRepository.save(user));
    }
}
