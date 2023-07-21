package com.lscavalcante.blog.service;

import com.lscavalcante.blog.configuration.exception.RecordException;
import com.lscavalcante.blog.configuration.exception.UnMappedException;
import com.lscavalcante.blog.configuration.security.SecurityConfiguration;
import com.lscavalcante.blog.dto.comment.RequestCreateComment;
import com.lscavalcante.blog.dto.comment.RequestUpdateComment;
import com.lscavalcante.blog.dto.comment.ResponseCommentDetail;
import com.lscavalcante.blog.model.blog.Blog;
import com.lscavalcante.blog.model.comment.Comment;
import com.lscavalcante.blog.model.user.User;
import com.lscavalcante.blog.repository.CommentRepository;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
public class CommentService {
    final CommentRepository commentRepository;
    final ModelMapperService modelMapperService;
    final BlogService blogService;
    public CommentService(CommentRepository commentRepository, BlogService blogService, ModelMapperService modelMapperService) {
        this.commentRepository = commentRepository;
        this.blogService = blogService;
        this.modelMapperService = modelMapperService;
    }

    public ResponseCommentDetail createComment(Long blogId, RequestCreateComment request) {
        try{
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            User user = (User) authentication.getPrincipal();

            Blog blog = blogService.findBlogById(blogId, true);

            Comment comment = new Comment();
            comment.setBlog(blog);
            comment.setContent(request.getContent());
            comment.setAuthor(user);
            commentRepository.save(comment);

            return modelMapperService.map(comment, ResponseCommentDetail.class);
        } catch (RuntimeException ex) {
            throw ex;
        } catch (Exception ex) {
            throw new UnMappedException(ex.getMessage());
        }
    }

    public ResponseCommentDetail updateComment(Long commentId, Long blogId, RequestUpdateComment request) {
        try{

            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            User user = (User) authentication.getPrincipal();

            Comment  comment = commentRepository.findById(commentId).orElseThrow(() -> new RecordException("Comment not found"));
            blogService.findBlogById(blogId, true);

            // rules before the update

            if(!comment.getAuthor().getId().equals(user.getId())) {
                throw new RuntimeException("You aren't authorized to update this comment, you aren't the author");
            }

            // update the blog

            modelMapperService.map(request, comment);
            commentRepository.save(comment);

            return modelMapperService.map(comment, ResponseCommentDetail.class);
        } catch (RuntimeException ex) {
            throw ex;
        } catch (Exception ex) {
            throw new UnMappedException(ex.getMessage());
        }
    }

    public boolean deleteComment(Long blogId, Long commentId) {
        try{
            var comment = commentRepository.findById(commentId).orElseThrow(() -> new RecordException("Comment not found"));
            blogService.findBlogById(blogId, true);

            commentRepository.delete(comment);
            return true;
        } catch (RuntimeException ex) {
            throw ex;
        } catch (Exception ex) {
            throw new UnMappedException(ex.getMessage());
        }
    }
}
