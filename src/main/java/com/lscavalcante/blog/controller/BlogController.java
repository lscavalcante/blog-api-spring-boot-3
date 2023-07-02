package com.lscavalcante.blog.controller;

import com.lscavalcante.blog.dto.blog.RequestCreateBlog;
import com.lscavalcante.blog.dto.blog.ResponseDetailBlog;
import com.lscavalcante.blog.dto.blog.ResponseListBlog;
import com.lscavalcante.blog.dto.comment.RequestCreateComment;
import com.lscavalcante.blog.dto.comment.RequestUpdateComment;
import com.lscavalcante.blog.dto.comment.ResponseCommentDetail;
import com.lscavalcante.blog.service.BlogService;
import com.lscavalcante.blog.service.CommentService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@Tag(name = "Blog")
@RestController
@RequestMapping("api/blogs")
public class BlogController {

    final BlogService blogService;
    final CommentService commentService;

    public BlogController(BlogService blogService, CommentService commentService) {
        this.blogService = blogService;
        this.commentService = commentService;
    }

    @GetMapping
    public ResponseEntity<List<ResponseListBlog>> list() {
        var body = blogService.list();
        return ResponseEntity.status(HttpStatus.CREATED).body(body);
    }

    @GetMapping("{blogId}")
    public ResponseEntity<ResponseDetailBlog> retrieve(@Validated @PathVariable Long blogId) {
        var body = blogService.retrieve(blogId);
        return ResponseEntity.status(HttpStatus.CREATED).body(body);
    }

    @PostMapping
    public ResponseEntity<ResponseDetailBlog> create(@RequestBody @Validated RequestCreateBlog request) {
        var body = blogService.create(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(body);
    }

    @PostMapping("{blogId}/comment")
    public ResponseEntity<ResponseCommentDetail> createComment(@Validated @PathVariable Long blogId, @Validated @RequestBody RequestCreateComment request) {
        var comment = commentService.createComment(blogId, request);
        return ResponseEntity.status(HttpStatus.CREATED).body(comment);
    }

    @PutMapping("{blogId}/comment/{commentId}")
    public ResponseEntity<ResponseCommentDetail> updateComment(@PathVariable Long blogId, @PathVariable Long commentId, @Validated @RequestBody RequestUpdateComment request) {
        var body = commentService.updateComment(commentId, blogId, request);
        return ResponseEntity.status(HttpStatus.OK).body(body);
    }

    @DeleteMapping("{blogId}/comment/{commentId}")
    public ResponseEntity<Void> deleteComment(@PathVariable Long blogId, @PathVariable Long commentId) {
        commentService.deleteComment(blogId, commentId);
        return ResponseEntity.noContent().build();
    }

}
