package com.lscavalcante.blog.dto.blog;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;

@Data
public class RequestCreateBlog {
    @NotEmpty
    private String title;
    @NotEmpty
    private String content;
    @NotNull
    private MultipartFile image;
}
