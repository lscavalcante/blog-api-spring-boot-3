package com.lscavalcante.blog.repository;

import com.lscavalcante.blog.model.blog.Blog;
import com.lscavalcante.blog.model.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BlogRepository extends JpaRepository<Blog, Long> {
}
