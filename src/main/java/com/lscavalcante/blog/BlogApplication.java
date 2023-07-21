package com.lscavalcante.blog;

import com.lscavalcante.blog.model.blog.Blog;
import com.lscavalcante.blog.model.user.Role;
import com.lscavalcante.blog.repository.BlogRepository;
import com.lscavalcante.blog.repository.RoleRepository;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.util.List;

@SpringBootApplication
public class BlogApplication {

    public static void main(String[] args) {
        SpringApplication.run(BlogApplication.class, args);
    }

    final RoleRepository roleRepository;
    final BlogRepository blogRepository;

    public BlogApplication(RoleRepository roleRepository, BlogRepository blogRepository
    ) {
        this.roleRepository = roleRepository;
        this.blogRepository = blogRepository;
    }

    @Bean
    void initConfiguration() {

        List<String> nameRoles = List.of("ADMIN", "DEFAULT");

        for (var name : nameRoles) {
            Role role = new Role();
            Blog blog = new Blog();
            blog.setTitle(name);
            blog.setContent(name);
            role.setName(name);
            roleRepository.save(role);
            blogRepository.save(blog);
        }

    }


}
