package com.lscavalcante.blog.repository;

import com.lscavalcante.blog.model.blog.Blog;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface BlogRepository extends JpaRepository<Blog, Long>, JpaSpecificationExecutor<Blog> {
    Page<Blog> findAllByTitleContainingIgnoreCaseAndContentContainingIgnoreCase(String title, String content, Pageable pageable);

    Page<Blog> findAllByTitleContaining(String title, Pageable pageable);

    Page<Blog> findByTitleLikeOrContentLike(String title, String content, Pageable pageable);

    Page<Blog> findAllByTitleContainingIgnoreCaseOrContentContainingIgnoreCaseAndUserId(String title, String content, Long userId, Pageable pageable);

}
