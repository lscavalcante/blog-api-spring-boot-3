package com.lscavalcante.blog;

import com.lscavalcante.blog.model.user.Role;
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

    public BlogApplication(RoleRepository roleRepository
    ) {
        this.roleRepository = roleRepository;
    }

    @Bean
    void initConfiguration() {

        List<String> nameRoles = List.of("ADMIN", "DEFAULT");

        for (var name : nameRoles) {
            Role role = new Role();
            role.setName(name);
            roleRepository.save(role);
        }

    }


}
