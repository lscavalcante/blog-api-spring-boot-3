package com.lscavalcante.blog.service;

import com.lscavalcante.blog.dto.upload.ResponseUpload;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Objects;
import org.springframework.core.io.Resource;

@Service
public class UploadService {

    public ResponseUpload save(MultipartFile file) throws IOException {
        Path currentPath = Paths.get("uploads").toAbsolutePath().normalize();
        // create directory
        Files.createDirectories(currentPath);
        // save imagem in dictory
        String filename = StringUtils.cleanPath(Objects.requireNonNull(file.getOriginalFilename()));
        if (filename.contains("..") || filename.isEmpty()) {
            throw new RuntimeException("Filename contains invalid path sequence" + filename);
        }

        ResponseUpload responseUpload = new ResponseUpload();
        responseUpload.setRealName(filename);
        responseUpload.setName(filename);
        responseUpload.setSize(file.getSize());

        // check if file already exists
        Path targetLocation = currentPath.resolve(filename);
        if (Files.exists(targetLocation)) {
            // file already exists, so rename it
            String newFilename = System.currentTimeMillis() + "_" + filename;
            targetLocation = currentPath.resolve(newFilename);
            responseUpload.setName(newFilename);
        }

        // copy file to new location
        Files.copy(file.getInputStream(), targetLocation, StandardCopyOption.REPLACE_EXISTING);

        responseUpload.setPath(ServletUriComponentsBuilder.fromCurrentContextPath().path("api/file/v1/dowloadFile/").path(responseUpload.getName()).toUriString());
        return responseUpload;
    }

    public ResponseUpload save(MultipartFile file, boolean exclude) throws IOException {
        Path currentPath = Paths.get("uploads").toAbsolutePath().normalize();
        // create directory
        Files.createDirectories(currentPath);
        // save imagem in dictory
        String filename = StringUtils.cleanPath(Objects.requireNonNull(file.getOriginalFilename()));
        if (filename.contains("..") || filename.isEmpty()) {
            throw new RuntimeException("Filename contains invalid path sequence" + filename);
        }

        ResponseUpload responseUpload = new ResponseUpload();
        responseUpload.setRealName(filename);
        responseUpload.setName(filename);
        responseUpload.setSize(file.getSize());

        // check if file already exists
        Path targetLocation = currentPath.resolve(filename);
        if (Files.exists(targetLocation) && !exclude) {
            // file already exists, so rename it
            String newFilename = System.currentTimeMillis() + "_" + filename;
            targetLocation = currentPath.resolve(newFilename);
            responseUpload.setName(newFilename);
        }

        // copy file to new location
        Files.copy(file.getInputStream(), targetLocation, StandardCopyOption.REPLACE_EXISTING);

        responseUpload.setPath(ServletUriComponentsBuilder.fromCurrentContextPath().path("api/file/v1/dowloadFile/").path(responseUpload.getName()).toUriString());
        return responseUpload;
    }

    public ResponseUpload save(String urlPath, MultipartFile file) throws IOException {
        Path currentPath = Paths.get(urlPath).toAbsolutePath().normalize();
        // create directory
        Files.createDirectories(currentPath);
        // save imagem in dictory
        String filename = StringUtils.cleanPath(Objects.requireNonNull(file.getOriginalFilename()));
        if (filename.contains("..") || filename.isEmpty()) {
            throw new RuntimeException("Filename contains invalid path sequence" + filename);
        }

        ResponseUpload responseUpload = new ResponseUpload();
        responseUpload.setRealName(filename);
        responseUpload.setName(filename);
        responseUpload.setSize(file.getSize());

        // check if file already exists
        Path targetLocation = currentPath.resolve(filename);
        if (Files.exists(targetLocation)) {
            // file already exists, so rename it
            String newFilename = System.currentTimeMillis() + "_" + filename;
            targetLocation = currentPath.resolve(newFilename);
            responseUpload.setName(newFilename);
        }

        // copy file to new location
        Files.copy(file.getInputStream(), targetLocation, StandardCopyOption.REPLACE_EXISTING);

        responseUpload.setPath(ServletUriComponentsBuilder.fromCurrentContextPath().path("api/file/v1/dowloadFile/").path(responseUpload.getName()).toUriString());
        return responseUpload;
    }

    public Resource show(String filename) throws IOException {
        Path imagePath = Paths.get("uploads").resolve(filename).toAbsolutePath().normalize();
        if (!Files.exists(imagePath) || Files.isDirectory(imagePath)) {
            throw new RuntimeException("Imagem não encontrada");
        }

        return new UrlResource(imagePath.toUri());
    }

    public Resource show(String urlPath, String filename) throws IOException {
        Path imagePath = Paths.get(urlPath).resolve(filename).toAbsolutePath().normalize();

        if (!Files.exists(imagePath) || Files.isDirectory(imagePath)) {
            throw new RuntimeException("Imagem não encontrada");
        }

        return new UrlResource(imagePath.toUri());
    }

    public void delete(String filename) {
        try {
            Path imagePath = Paths.get("uploads").resolve(filename).toAbsolutePath().normalize();
            if (Files.exists(imagePath) || !Files.isDirectory(imagePath)) {
                Files.delete(imagePath);
            }
        } catch (Exception e) {
            System.out.println("Image not found or exception when tried to delete image" + e.toString());
        }
    }

}
