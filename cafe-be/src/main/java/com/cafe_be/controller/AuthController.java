package com.cafe_be.controller;

import com.cafe_be.dto.LoginRequest;
import com.cafe_be.dto.LoginResponse;
import com.cafe_be.model.User;
import com.cafe_be.service.AuthService;
import com.cafe_be.service.TokenService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;
    private final TokenService tokenService;
    private final PasswordEncoder passwordEncoder;

    public AuthController(AuthService authService, TokenService tokenService, PasswordEncoder passwordEncoder) {
        this.authService = authService;
        this.tokenService = tokenService;
        this.passwordEncoder = passwordEncoder;
    }

    @PostMapping("/register")
    public User register(@RequestBody User u) { return authService.register(u); }

    @PostMapping("/login")
    public LoginResponse login(@RequestBody LoginRequest req) {
        User user = authService.findByUsername(req.getUsername());
        if (user == null) {
            throw new RuntimeException("Invalid credentials");
        }
        if (!passwordEncoder.matches(req.getPassword(), user.getPassword())) {
            throw new RuntimeException("Invalid credentials");
        }
        String token = tokenService.createToken(user.getUsername());
        return new LoginResponse(token, user.getUsername());
    }
}
