package com.bsmm.login.service.impl;

import com.bsmm.login.config.JwtUtils;
import com.bsmm.login.models.Role;
import com.bsmm.login.models.User;
import com.bsmm.login.models.enums.ERole;
import com.bsmm.login.repository.RoleRepository;
import com.bsmm.login.repository.UserRepository;
import com.bsmm.login.service.UserService;
import com.bsmm.login.service.dto.LoginRequest;
import com.bsmm.login.service.dto.LoginResponse;
import com.bsmm.login.service.dto.UserDTO;
import com.bsmm.login.service.dto.UserSignup;
import com.bsmm.login.service.dto.impl.UserDetailsImpl;
import com.bsmm.login.service.mapper.UserMapper;
import lombok.RequiredArgsConstructor;
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
import java.util.Set;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder encoder;
    private final AuthenticationManager authenticationManager;
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

        User user = new User(dto.getUsername(), dto.getEmail(), encoder.encode(dto.getPassword()));

        Set<Role> roles = getRoles(dto);
        user.setRoles(roles);
        user.setIsActive(Boolean.TRUE);

        return UserMapper.INSTANCE.toDto(userRepository.save(user));
    }

    private Set<Role> getRoles(UserSignup dto) {
        Set<String> strRoles = dto.getRole();
        Set<Role> roles = new HashSet<>();
        strRoles.forEach(role -> {
            if (role.equals("admin")) {
                Role adminRole = roleRepository.findByName(ERole.ROLE_ADMIN).orElseThrow(() -> new RuntimeException("Error: Role is not found."));
                roles.add(adminRole);
            } else {
                Role userRole = roleRepository.findByName(ERole.ROLE_USER).orElseThrow(() -> new RuntimeException("Error: Role is not found."));
                roles.add(userRole);
            }
        });
        return roles;
    }
}
