package com.lscavalcante.blog.configuration.exception;


import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class RecordException extends RuntimeException {

    public RecordException(String message) {
        super(message);
    }
}