package com.lscavalcante.blog.dto.auth;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class RequestResetPassword {
    @Email
    @NotBlank
    private String email;

    @Size(min = 6, max = 6)
    @NotBlank
    private String resetToken;

    @NotBlank
    @Size(min = 4)
    private String password;
}
