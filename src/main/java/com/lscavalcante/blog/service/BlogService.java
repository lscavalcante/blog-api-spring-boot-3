package com.lscavalcante.blog.service;

import com.lscavalcante.blog.configuration.exception.NotFoundException;
import com.lscavalcante.blog.configuration.exception.UnMappedException;
import com.lscavalcante.blog.dto.auth.ResponseCreateAccount;
import com.lscavalcante.blog.dto.blog.RequestCreateBlog;
import com.lscavalcante.blog.dto.blog.ResponseDetailBlog;
import com.lscavalcante.blog.model.blog.Blog;
import com.lscavalcante.blog.repository.BlogRepository;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;


@Service
public class BlogService {

    final BlogRepository blogRepository;
    final ModelMapperService modelMapperService;

    public BlogService(BlogRepository blogRepository, ModelMapperService modelMapperService) {
        this.blogRepository = blogRepository;
        this.modelMapperService = modelMapperService;
    }

    public ResponseDetailBlog create(RequestCreateBlog request) {
        try {
//            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            var blog = modelMapperService.map(request, Blog.class);
            blogRepository.save(blog);

            return modelMapperService.map(blog, ResponseDetailBlog.class);
        } catch (RuntimeException ex) {
            throw ex;
        } catch (Exception ex) {
            throw new UnMappedException(ex.getMessage());
        }
    }
}
