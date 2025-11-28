package com.example.auth;

import java.util.HashMap;
import java.util.Map;

// Простая in-memory реализация для интеграционного тестирования
public class InMemoryUserRepository implements UserRepository {
    private final Map<String, User> users = new HashMap<>();

    @Override
    public User findByUsername(String username) {
        return users.get(username);
    }

    @Override
    public void save(User user) {
        users.put(user.getUsername(), user);
    }

    @Override
    public boolean existsByUsername(String username) {
        return users.containsKey(username);
    }

    // Метод для очистки базы между тестами
    public void clear() {
        users.clear();
    }
}