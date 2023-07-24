package com.lscavalcante.blog.controller;

import com.lscavalcante.blog.dto.blog.RequestCreateBlog;
import com.lscavalcante.blog.dto.blog.RequestUpdateBlog;
import com.lscavalcante.blog.dto.blog.ResponseDetailBlog;
import com.lscavalcante.blog.dto.blog.ResponseListBlog;
import com.lscavalcante.blog.dto.comment.RequestCreateComment;
import com.lscavalcante.blog.dto.comment.RequestUpdateComment;
import com.lscavalcante.blog.dto.comment.ResponseCommentDetail;
import com.lscavalcante.blog.model.blog.Blog;
import com.lscavalcante.blog.service.BlogService;
import com.lscavalcante.blog.service.CommentService;
import com.lscavalcante.blog.service.PdfService;
import com.lscavalcante.blog.service.UploadService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@Tag(name = "Blog")
@RestController
@RequestMapping("api/blogs")
public class BlogController {

    final BlogService blogService;
    final CommentService commentService;

    final UploadService uploadService;
    final PdfService pdfService;

    public BlogController(BlogService blogService, CommentService commentService, UploadService uploadService, PdfService pdfService) {
        this.blogService = blogService;
        this.commentService = commentService;
        this.uploadService = uploadService;
        this.pdfService = pdfService;
    }

    @GetMapping
    public ResponseEntity<Page<ResponseListBlog>> list(
            @RequestParam(value = "title", defaultValue = "", required = false) String title,
            @RequestParam(value = "content", defaultValue = "", required = false) String content,
            @PageableDefault(size = Integer.MAX_VALUE) Pageable pageable
    ) {
        var body = blogService.list(title, content, pageable);

        SecurityContextHolder.getContext().getAuthentication();

        return ResponseEntity.status(HttpStatus.OK).body(body);
    }

    @GetMapping("{blogId}")
    public ResponseEntity<ResponseDetailBlog> show(@Validated @PathVariable Long blogId) {
        var body = blogService.show(blogId);
        return ResponseEntity.status(HttpStatus.OK).body(body);
    }

    @GetMapping("{blogId}/pdf")
    public ResponseEntity<Resource> showBlogPdf(@Validated @PathVariable Long blogId) throws IOException {

        var pdf = pdfService.createPdf();

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_PDF);

        return ResponseEntity.ok()
                .headers(headers)
                .body(pdf);
    }

    @GetMapping("users/{userId}")
    public ResponseEntity<Page<Blog>> blogsByUsers(
            @PathVariable Long userId,
            @RequestParam(value = "search", defaultValue = "", required = false) String search,
            @PageableDefault(size = Integer.MAX_VALUE) Pageable pageable) {
        var body = blogService.getAllBlogsByUserId(userId, search, pageable);
        return ResponseEntity.status(HttpStatus.OK).body(body);
    }

    @PutMapping(value = "{blogId}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ResponseDetailBlog> update(@Validated @PathVariable Long blogId, @Validated @ModelAttribute RequestUpdateBlog request) {
        var body = blogService.update(blogId, request);
        return ResponseEntity.status(HttpStatus.OK).body(body);
    }

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ResponseDetailBlog> create(@Validated @ModelAttribute RequestCreateBlog request) {
        var body = blogService.create(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(body);
    }

    @PostMapping("{blogId}/comment")
    public ResponseEntity<ResponseCommentDetail> createComment(@Validated @PathVariable Long blogId, @Validated @RequestBody RequestCreateComment request) {
        var comment = commentService.createComment(blogId, request);
        return ResponseEntity.status(HttpStatus.OK).body(comment);
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
