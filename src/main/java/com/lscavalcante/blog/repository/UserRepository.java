package com.lscavalcante.blog.repository;

import com.lscavalcante.blog.model.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface  UserRepository  extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);
}
