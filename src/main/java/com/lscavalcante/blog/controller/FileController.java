package com.lscavalcante.blog.controller;

import com.lscavalcante.blog.service.UploadService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;


@Tag(name = "Files")
@RestController
@RequestMapping("files")
public class FileController {
    private final UploadService uploadService;

    public FileController(UploadService uploadService) {
        this.uploadService = uploadService;
    }

    @GetMapping("{filename:.+}")
    public ResponseEntity<Resource> download(@PathVariable String filename, HttpServletRequest httpServletRequest) throws IOException {
        Resource resource = uploadService.show("uploads", filename);
        String contentyType = "";
        contentyType = httpServletRequest.getServletContext().getMimeType(resource.getFile().getAbsolutePath());

        if (contentyType.isBlank()) contentyType = MediaType.APPLICATION_OCTET_STREAM_VALUE;

        return ResponseEntity.ok().contentType(MediaType.parseMediaType(contentyType)).body(resource);
    }
}
