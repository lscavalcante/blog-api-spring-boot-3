package com.lscavalcante.blog.dto.upload;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ResponseUpload {
    private String name;
    private String path;
    private String realName;
    private Long size;
}
