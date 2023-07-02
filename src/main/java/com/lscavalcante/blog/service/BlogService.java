package com.lscavalcante.blog.service;

import com.lscavalcante.blog.configuration.exception.NotFoundException;
import com.lscavalcante.blog.configuration.exception.RecordException;
import com.lscavalcante.blog.configuration.exception.UnMappedException;
import com.lscavalcante.blog.dto.auth.ResponseCreateAccount;
import com.lscavalcante.blog.dto.blog.RequestCreateBlog;
import com.lscavalcante.blog.dto.blog.ResponseDetailBlog;
import com.lscavalcante.blog.dto.blog.ResponseListBlog;
import com.lscavalcante.blog.model.blog.Blog;
import com.lscavalcante.blog.model.user.Role;
import com.lscavalcante.blog.model.user.User;
import com.lscavalcante.blog.repository.BlogRepository;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


@Service
public class BlogService {

    final BlogRepository blogRepository;
    final ModelMapperService modelMapperService;

    public BlogService(BlogRepository blogRepository, ModelMapperService modelMapperService) {
        this.blogRepository = blogRepository;
        this.modelMapperService = modelMapperService;
    }


    public List<ResponseListBlog> list() {
        try {
            return blogRepository.findAll().stream().map((e) -> modelMapperService.map(e, ResponseListBlog.class)).toList();
        } catch (RuntimeException ex) {
            throw ex;
        } catch (Exception ex) {
            throw new UnMappedException(ex.getMessage());
        }
    }

    public ResponseDetailBlog retrieve(Long blogId) {
        try {

            var blog = findBlogById(blogId, true);

            return modelMapperService.map(blog, ResponseDetailBlog.class);
        } catch (RuntimeException ex) {
            throw ex;
        } catch (Exception ex) {
            throw new UnMappedException(ex.getMessage());
        }
    }

    public ResponseDetailBlog create(RequestCreateBlog request) {
        try {
//            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            var blog = modelMapperService.map(request, Blog.class);
            blog.setComments(new ArrayList<>());
            blogRepository.save(blog);

            return modelMapperService.map(blog, ResponseDetailBlog.class);
        } catch (RuntimeException ex) {
            throw ex;
        } catch (Exception ex) {
            throw new UnMappedException(ex.getMessage());
        }
    }

    public Blog findBlogById(Long blogId, boolean raiseException) throws RuntimeException {
        try {
            Blog blog = blogRepository.findById(blogId).orElse(null);

            if (raiseException && blog == null) {
                throw new RecordException("Blog does not exist");
            }

            return blog;
        } catch (RuntimeException ex) {
            throw ex;
        } catch (Exception ex) {
            throw new UnMappedException(ex.getMessage());
        }
    }
}
