package com.cafe_be.config;

import com.cafe_be.model.*;
import com.cafe_be.model.enums.Role;
import com.cafe_be.repository.CafeTableRepository;
import com.cafe_be.repository.ProductRepository;
import com.cafe_be.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class DataLoader {

    @Bean
    CommandLineRunner init(UserRepository userRepo, ProductRepository productRepo, CafeTableRepository tableRepo, PasswordEncoder encoder) {
        return args -> {
            if (userRepo.findByUsername("root").isEmpty()) {
                User root = new User();
                root.setUsername("root");
                root.setPassword(encoder.encode("rootpass"));
                root.setRoles(java.util.Set.of(Role.ROOT, Role.ADMIN));
                root.setFullName("Root User");
                userRepo.save(root);
            }
        };
    }
}
