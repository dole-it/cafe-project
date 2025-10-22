package com.cafe_be.controller;

import com.cafe_be.model.User;
import com.cafe_be.repository.UserRepository;
import com.cafe_be.service.AuthService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserRepository userRepository;
    private final AuthService authService;

    public UserController(UserRepository userRepository, AuthService authService) {
        this.userRepository = userRepository;
        this.authService = authService;
    }

    @GetMapping
    public List<User> list() {
        return userRepository.findAll();
    }

    @PostMapping
    public User create(@RequestBody User u) {
        // delegate to authService to encode password and set default role
        return authService.register(u);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) { userRepository.deleteById(id); }
}
