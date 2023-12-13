package com.bsmm.login.controllers;

import com.bsmm.login.service.UserService;
import com.bsmm.login.service.dto.*;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequiredArgsConstructor
@RequestMapping("/api/auth")
public class UserController {
    private final UserService userService;

    @PostMapping("/sign-in")
    public ResponseEntity<LoginResponse> authenticateUser(@Valid @RequestBody LoginRequest loginRequest) {
        return ResponseEntity.ok(userService.authenticateUser(loginRequest));
    }

    @PostMapping("/sign-up")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<UserDTO> create(@RequestBody @Valid UserSignup dto) {
        return ResponseEntity.ok(userService.create(dto));
    }

    @PostMapping("/sign-out")
    public ResponseEntity<MessageResponse> logoutUser() {
        return ResponseEntity.ok().body(new MessageResponse("You've been signed out!"));
    }
    @GetMapping
    @PreAuthorize("hasRole('USER')  or hasRole('ADMIN')")
    public ResponseEntity<List<UserDTO>> userAccess() {
        return ResponseEntity.ok(userService.getAll());
    }
}
