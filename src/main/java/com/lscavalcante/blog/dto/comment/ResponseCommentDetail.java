package com.lscavalcante.blog.dto.comment;

import lombok.Data;
import java.util.Date;

@Data
public class ResponseCommentDetail {
    private Long id;
    private String content;
    private Date createdAt;
    private Date updatedAt;
//    private User author;
//    private Blog blog;
}
