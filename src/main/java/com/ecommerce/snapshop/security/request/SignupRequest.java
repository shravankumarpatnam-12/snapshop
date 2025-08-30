package com.ecommerce.snapshop.security.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.util.Set;

@Data
public class SignupRequest {
    @NotBlank
    @Size(min = 3, max = 25)
    private String userName;

    @NotBlank
    @Size(min = 8, max = 56)
    private String password;

    @NotBlank
    @Size(max =50)
    @Email
    private String email;
    private Set<String> roles;
}
