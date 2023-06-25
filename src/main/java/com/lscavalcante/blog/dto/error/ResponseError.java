package com.lscavalcante.blog.dto.error;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;

import java.util.List;
import java.util.Map;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ResponseError {
    private HttpStatus status;
    private String message;
    private List<Map<String, Object>> errors;
}
