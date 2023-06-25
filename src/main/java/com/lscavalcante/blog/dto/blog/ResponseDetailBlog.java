package com.lscavalcante.blog.dto.blog;

import lombok.Data;

import java.util.Date;

@Data
public class ResponseDetailBlog {
    private Long id;
    private String title;
    private String content;
    private Date createdAt;
    private Date updatedAt;
}
