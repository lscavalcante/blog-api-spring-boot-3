package com.lscavalcante.blog.dto.comment;


import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class RequestCreateComment {

    @NotBlank
    @Size(min = 3)
    private String content;
}
