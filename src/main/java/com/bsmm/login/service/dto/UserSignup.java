package com.bsmm.login.service.dto;

import jakarta.validation.constraints.*;
import lombok.Data;

import java.io.Serializable;
import java.util.Set;

@Data
public class UserSignup implements Serializable {
    @NotBlank
    @Size(min = 3, max = 20)
    private String username;

    @NotBlank
    @Size(max = 50)
    @Email
    private String email;

    @NotNull
    @NotEmpty
    private Set<String> role;

    @NotBlank
    @Size(min = 6, max = 40)
    private String password;
}
