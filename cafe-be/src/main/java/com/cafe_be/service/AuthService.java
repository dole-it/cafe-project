package com.cafe_be.service;

import com.cafe_be.model.User;

public interface AuthService {
    User register(User u);
    User findByUsername(String username);
}
