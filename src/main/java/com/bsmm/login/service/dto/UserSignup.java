package com.bsmm.login.service.dto;

import com.bsmm.login.models.enums.ERole;
import jakarta.validation.constraints.*;
import lombok.Data;

import java.io.Serializable;
import java.util.Set;

@Data
public class UserSignup implements Serializable {
    @NotBlank
    @Size(max = 150)
    private String fullName;

    @NotBlank
    @Email
    @Size(min = 5, max = 50)
    private String username;

    @NotNull
    @NotEmpty
    private Set<ERole> roles;

    @NotBlank
    @Size(min = 8, max = 25)
    private String password;
}
