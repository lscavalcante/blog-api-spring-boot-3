package com.lscavalcante.blog.controller;

import com.lscavalcante.blog.dto.user.ResponseDetailUser;
import com.lscavalcante.blog.service.UserService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@Tag(name = "User")
@RestController
@RequestMapping("/api/users")
class UserController {

    final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping()
    public ResponseEntity<Page<ResponseDetailUser>> list(@PageableDefault(size = Integer.MAX_VALUE) Pageable pageable) {
        return ResponseEntity.status(HttpStatus.OK).body(userService.list(pageable));
    }
    @PostMapping()
    public ResponseEntity<String> create() {
        return ResponseEntity.status(HttpStatus.CREATED).body("create user");
    }
    @GetMapping("{id}")
    public ResponseEntity<String> retrieve(@PathVariable Long id) {
        return ResponseEntity.status(HttpStatus.CREATED).body("retrieve user");
    }
    @PutMapping("{id}")
    public ResponseEntity<String> update(@Validated @PathVariable Long id) {
        return ResponseEntity.status(HttpStatus.OK).body(id.toString() + " update user");
    }
    @DeleteMapping("{id}")
    public ResponseEntity<String> delete(@PathVariable Long id) {
        return ResponseEntity.status(HttpStatus.CREATED).body("create user");
    }

}