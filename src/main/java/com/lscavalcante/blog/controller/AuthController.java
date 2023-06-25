package com.lscavalcante.blog.controller;

import com.lscavalcante.blog.dto.auth.*;
import com.lscavalcante.blog.dto.token.ResponseToken;
import com.lscavalcante.blog.service.UserService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Auth")
@RestController
@RequestMapping("api/auth")
public class AuthController {
    final UserService userService;

    public AuthController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("login")
    public ResponseEntity<ResponseToken> login(@Validated @RequestBody RequestLogin request) {
        var body = userService.login(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(body);
    }

    @PostMapping("createAccount")
    public ResponseEntity<ResponseCreateAccount> createAccount(@Validated @RequestBody RequestCreateAccount request) {
        var response = userService.createAccount(request);

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PostMapping("forgetPassword")
    public ResponseEntity<ResponseForgetPassword> forgetPassword(@Validated @RequestBody RequestForgetPassword request) {
        var body = userService.forgetPassword(request);

        return ResponseEntity.status(HttpStatus.OK).body(body);
    }

    @PostMapping("resetPassword")
    public ResponseEntity<ResponseResetPassword> recoverPassword(@Validated @RequestBody RequestResetPassword request) {
        var body = userService.resetPassword(request);

        return ResponseEntity.status(HttpStatus.OK).body(body);
    }
}
