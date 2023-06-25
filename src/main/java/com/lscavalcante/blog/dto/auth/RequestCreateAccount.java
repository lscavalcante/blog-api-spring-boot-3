package com.lscavalcante.blog.dto.auth;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class RequestCreateAccount {
    @Email
    @NotBlank
    private String email;

    @Size(min = 3)
    @NotBlank
    private String password;
}
