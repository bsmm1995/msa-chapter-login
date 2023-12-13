package com.bsmm.login.controllers;

import com.bsmm.login.service.UserService;
import com.bsmm.login.service.dto.LoginRequest;
import com.bsmm.login.service.dto.LoginResponse;
import com.bsmm.login.service.dto.UserDTO;
import com.bsmm.login.service.dto.UserSignup;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class UserController {
    private final UserService userService;

    @PostMapping("/auth/sign-in")
    public ResponseEntity<LoginResponse> authenticateUser(@Valid @RequestBody LoginRequest loginRequest) {
        return ResponseEntity.ok(userService.authenticateUser(loginRequest));
    }

    @PostMapping("/auth/sign-out")
    public ResponseEntity<Void> logoutUser(@RequestHeader("Autorization") String token) {
        userService.logoutUser(token);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/users")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<UserDTO> create(@RequestBody @Valid UserSignup dto) {
        return ResponseEntity.ok(userService.create(dto));
    }


    @GetMapping("/users")
    @PreAuthorize("hasRole('USER')  or hasRole('ADMIN')")
    public ResponseEntity<List<UserDTO>> userAccess() {
        return ResponseEntity.ok(userService.getAll());
    }
}
