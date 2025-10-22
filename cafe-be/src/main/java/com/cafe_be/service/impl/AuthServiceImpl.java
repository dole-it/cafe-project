package com.cafe_be.service.impl;

import com.cafe_be.model.enums.Role;
import com.cafe_be.model.User;
import com.cafe_be.repository.UserRepository;
import com.cafe_be.service.AuthService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public AuthServiceImpl(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public User register(User u) {
        u.setPassword(passwordEncoder.encode(u.getPassword()));
        u.setRoles(Set.of(Role.USER));
        return userRepository.save(u);
    }

    @Override
    public User findByUsername(String username) {
        return userRepository.findByUsername(username).orElse(null);
    }
}
