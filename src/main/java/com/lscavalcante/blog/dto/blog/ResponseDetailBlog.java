package com.lscavalcante.blog.dto.blog;

import com.lscavalcante.blog.dto.comment.ResponseCommentDetail;
import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
public class ResponseDetailBlog {
    private Long id;
    private String title;
    private String content;
    private Date createdAt;
    private Date updatedAt;
    private List<ResponseCommentDetail> comments;
}
