package com.lscavalcante.blog.dto.auth;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class RequestForgetPassword {
    @Email
    @NotBlank
    private String email;
}
