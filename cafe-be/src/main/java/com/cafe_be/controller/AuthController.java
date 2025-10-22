package com.cafe_be.controller;

import com.cafe_be.dto.LoginRequest;
import com.cafe_be.dto.LoginResponse;
import com.cafe_be.dto.UserDTO;
import com.cafe_be.model.User;
import com.cafe_be.service.AuthService;
import com.cafe_be.service.TokenService;

import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

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

    @GetMapping("/me")
    public UserDTO me() {
        var auth = org.springframework.security.core.context.SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || !auth.isAuthenticated() || auth.getName() == null) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
        }
        String username = auth.getName();
        User user = authService.findByUsername(username);
        if (user == null) throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
        return new UserDTO(user.getId(), user.getUsername(), user.getEmail(), user.getRole(), user.getFullName());
    }

    @PostMapping("/register")
    public User register(@RequestBody User u) { return authService.register(u); }

    @PostMapping("/login")
    public LoginResponse login(@RequestBody LoginRequest req) {
        User user = authService.findByUsername(req.getUsername());
        if (user == null || !passwordEncoder.matches(req.getPassword(), user.getPassword())) {
            throw new org.springframework.web.server.ResponseStatusException(org.springframework.http.HttpStatus.UNAUTHORIZED, "Invalid credentials");
        }

        try {
            String token = tokenService.createToken(user);
            UserDTO userDto = new UserDTO(user.getId(), user.getUsername(), user.getEmail(), user.getRole(), user.getFullName());
            return new LoginResponse(token, userDto);
        } catch (Exception ex) {
            throw new org.springframework.web.server.ResponseStatusException(org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR, "Failed to create token");
        }
    }
}
