package com.lscavalcante.blog.controller;

import com.lscavalcante.blog.dto.blog.RequestCreateBlog;
import com.lscavalcante.blog.dto.blog.ResponseDetailBlog;
import com.lscavalcante.blog.service.BlogService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Blog")
@RestController
@RequestMapping("api/blogs")
public class BlogController {

    final BlogService blogService;

    public BlogController(BlogService blogService) {
        this.blogService = blogService;
    }

    @PostMapping
    public ResponseEntity<ResponseDetailBlog> create(@RequestBody @Validated RequestCreateBlog request) {

        var body = blogService.create(request);

        return ResponseEntity.status(HttpStatus.CREATED).body(body);
    }

}
