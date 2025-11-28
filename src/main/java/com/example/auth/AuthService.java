package com.example.auth;

public class AuthService {
    private final UserRepository userRepository;

    public AuthService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public boolean login(String username, String password) {
        if (username == null || password == null) {
            throw new IllegalArgumentException("Username and password cannot be null");
        }

        User user = userRepository.findByUsername(username);

        if (user == null) {
            return false; // Пользователь не найден
        }

        if (!user.isActive()) {
            return false; // Пользователь неактивен
        }

        return user.getPassword().equals(password); // Проверка пароля
    }

    public boolean register(String username, String password) {
        if (username == null || password == null || username.trim().isEmpty()) {
            throw new IllegalArgumentException("Invalid username or password");
        }

        if (userRepository.existsByUsername(username)) {
            return false; // Пользователь уже существует
        }

        User newUser = new User(username, password, true);
        userRepository.save(newUser);
        return true;
    }
}