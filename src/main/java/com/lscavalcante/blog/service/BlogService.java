package com.lscavalcante.blog.service;

import com.lscavalcante.blog.configuration.exception.NotFoundException;
import com.lscavalcante.blog.configuration.exception.RecordException;
import com.lscavalcante.blog.configuration.exception.UnMappedException;
import com.lscavalcante.blog.configuration.sql.BlogSpecifications;
import com.lscavalcante.blog.dto.blog.RequestCreateBlog;
import com.lscavalcante.blog.dto.blog.RequestUpdateBlog;
import com.lscavalcante.blog.dto.blog.ResponseDetailBlog;
import com.lscavalcante.blog.dto.blog.ResponseListBlog;
import com.lscavalcante.blog.dto.upload.ResponseUpload;
import com.lscavalcante.blog.model.blog.Blog;
import com.lscavalcante.blog.model.user.User;
import com.lscavalcante.blog.repository.BlogRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.util.ArrayList;


@Service
public class BlogService {

    final BlogRepository blogRepository;
    final ModelMapperService modelMapperService;

    final UploadService uploadService;

    public BlogService(BlogRepository blogRepository, ModelMapperService modelMapperService, UploadService uploadService) {
        this.blogRepository = blogRepository;
        this.modelMapperService = modelMapperService;
        this.uploadService = uploadService;
    }

    public Page<ResponseListBlog> list(String title, String content, Pageable pageable) {
        try {
            return blogRepository.findAllByTitleContainingIgnoreCaseAndContentContainingIgnoreCase(title, content, pageable)
                    .map((e) -> {
                        ResponseListBlog responseListBlog = modelMapperService.map(e, ResponseListBlog.class);
                        responseListBlog.setImageUrl(getFullPathImageUrl(e.getImage()));
                        return responseListBlog;
                    });
        } catch (RuntimeException ex) {
            throw ex;
        } catch (Exception ex) {
            throw new UnMappedException(ex.getMessage());
        }
    }

    public ResponseDetailBlog show(Long blogId) {
        try {
            var blog = blogRepository.findById(blogId).orElseThrow(() -> new NotFoundException("Blog not found"));

            blog.setImage(getFullPathImageUrl(blog.getImage()));

            return modelMapperService.map(blog, ResponseDetailBlog.class);

        } catch (RuntimeException ex) {
            throw ex;
        } catch (Exception ex) {
            throw new UnMappedException(ex.getMessage());
        }
    }

    public Page<Blog> getAllBlogsWithSearch(String search, Pageable pageable) {
        if (search == null || search.isEmpty()) {
            return blogRepository.findAll(pageable);
        } else {

//            SearchCriteriaSpecification<Blog> spec = new SearchCriteriaSpecification<Blog>(
//                    new SearchCriteria("title", SearchOperation.LIKE, "%" + search + "%"),
//                    new SearchCriteria("title", SearchOperation.LIKE, "%" + search + "%")
//            );

//            Specification<Blog> spec = (root, criteriaQuery, criteriaBuilder) -> {
//                String searchStr = "%" + search + "%";
//                return criteriaBuilder.or(
////                        criteriaBuilder.like(
////                                root.get("id").as(String.class),
////                                searchStr
////                        ),
//                        criteriaBuilder.like(root.get("title"), searchStr),
//                        criteriaBuilder.like(root.get("content"), searchStr)
//                );
//            };

//            Specification<Blog> spec = (root, criteriaQuery, criteriaBuilder) -> {
//                return criteriaBuilder.or(
//                        criteriaBuilder.like(root.get("id"), "%Spring%"),
//                        criteriaBuilder.like(root.get("username"), "%Spring%"),
//                        criteriaBuilder.like(root.get("email"), "%Spring%")
//                );
//            };

            // Use the Specification object to filter the blogs
            Specification<Blog> spec = BlogSpecifications.search(search);
            return blogRepository.findAll(spec, pageable);
        }
    }

    public Page<Blog> getAllBlogsByUserId(Long userId, String search, Pageable pageable) {
//        if (search == null || search.isEmpty()) {
//            return new List.of([])
//        } else {
//
//        }
        Specification<Blog> spec = BlogSpecifications.search(search, userId);
        return blogRepository.findAll(spec, pageable);
    }

    public ResponseDetailBlog update(Long blogId, RequestUpdateBlog request) {
        try {

            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            User user = (User) authentication.getPrincipal();

            var blog = findBlogById(blogId, true);

            if (!blog.getUser().getId().equals(user.getId())) {
                throw new RuntimeException("you're not authorized to update this object, you're not the owner");
            }

            String filename = blog.getImage();

            MultipartFile newImage = request.getImage();
            if (newImage != null && !newImage.isEmpty()) {
                ResponseUpload createdImage = uploadService.save(newImage, true);
                // delete
                uploadService.delete(filename);
                // add new image name for model
                filename = createdImage.getName();
            }
            modelMapperService.map(request, blog);
            blog.setImage(filename);
            blogRepository.save(blog);

            return modelMapperService.map(blog, ResponseDetailBlog.class);
        } catch (RuntimeException ex) {
            throw ex;
        } catch (Exception ex) {
            throw new UnMappedException(ex.getMessage());
        }
    }

    public ResponseDetailBlog create(RequestCreateBlog request) {
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            User user = (User) authentication.getPrincipal();

            var blog = modelMapperService.map(request, Blog.class);
            blog.setComments(new ArrayList<>());
            blog.setUser(user);
            ResponseUpload responseUpload = uploadService.save(request.getImage());
            blog.setImage(responseUpload.getName());
            blogRepository.save(blog);

            blog.setImage(getFullPathImageUrl(blog.getImage()));

            return modelMapperService.map(blog, ResponseDetailBlog.class);
        } catch (RuntimeException ex) {
            throw ex;
        } catch (Exception ex) {
            throw new UnMappedException(ex.getMessage());
        }
    }

    public String getFullPathImageUrl(String filename) {
        if (filename == null || filename.isEmpty()) return null;

        return ServletUriComponentsBuilder.fromCurrentContextPath()
                .path("/files/")
                .path(filename)
                .toUriString();
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
