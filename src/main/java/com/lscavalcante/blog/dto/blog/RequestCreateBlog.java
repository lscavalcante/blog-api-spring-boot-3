package com.lscavalcante.blog.dto.blog;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
public class RequestCreateBlog {
    private String title;
    private String content;
}
