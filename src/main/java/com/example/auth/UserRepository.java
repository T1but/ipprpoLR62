package com.example.auth;

public interface UserRepository {
    User findByUsername(String username);
    void save(User user);
    boolean existsByUsername(String username);
}