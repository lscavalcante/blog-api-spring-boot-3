package com.lscavalcante.blog.configuration.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class UnMappedException extends RuntimeException {

    public UnMappedException(String message) {
        super(message);
    }
}
