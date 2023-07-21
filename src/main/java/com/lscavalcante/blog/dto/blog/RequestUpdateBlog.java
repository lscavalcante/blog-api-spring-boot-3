package com.lscavalcante.blog.dto.blog;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Null;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

@Data
public class RequestUpdateBlog {
    @NotEmpty
    private String title;
    @NotEmpty
    private String content;
    private MultipartFile image;
}
