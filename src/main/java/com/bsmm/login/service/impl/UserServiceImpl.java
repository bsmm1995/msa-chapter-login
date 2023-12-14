package com.bsmm.login.service.impl;

import com.bsmm.login.config.JwtUtils;
import com.bsmm.login.models.Role;
import com.bsmm.login.models.User;
import com.bsmm.login.models.enums.ERole;
import com.bsmm.login.repository.RoleRepository;
import com.bsmm.login.repository.UserRepository;
import com.bsmm.login.service.RedisService;
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

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder encoder;
    private final AuthenticationManager authenticationManager;
    private final UserDetailsServiceImpl userDetailsService;
    private final RedisService redisService;
    private final JwtUtils jwtUtils;

    @Override
    public LoginResponse authenticateUser(LoginRequest loginRequest) {
        Authentication authentication = authenticationManager
                .authenticate(new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));
        SecurityContextHolder.getContext().setAuthentication(authentication);
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        LoginResponse loginResponse = jwtUtils.getResponse(userDetails);
        redisService.saveSession(loginResponse);
        return loginResponse;
    }

    @Override
    public LoginResponse refreshToken(String token) {
        if (!jwtUtils.validateJwtToken(token)) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Toekn caucado");
        }
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
        user.setRoles(getUserRoles(dto.getRoles()));
        return UserMapper.INSTANCE.toDto(userRepository.save(user));
    }

    private Set<Role> getUserRoles(Set<ERole> roles) {
        Set<Role> roleSet = new HashSet<>();
        roles.forEach(eRole -> {
            Optional<Role> optionalRole = roleRepository.findByName(eRole);
            optionalRole.ifPresent(roleSet::add);
        });
        return roleSet;
    }
}
