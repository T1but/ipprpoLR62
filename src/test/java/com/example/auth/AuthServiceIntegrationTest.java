package com.example.auth;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class AuthServiceIntegrationTest {

    private InMemoryUserRepository userRepository;
    private AuthService authService;

    @BeforeEach
    void setUp() {
        userRepository = new InMemoryUserRepository();
        authService = new AuthService(userRepository);
    }

    @Test
    void testFullUserLifecycle() {
        // Регистрация нового пользователя
        String username = "integrationuser";
        String password = "integrationpass";

        boolean registrationResult = authService.register(username, password);
        assertTrue(registrationResult, "Registration should succeed");

        // Попытка повторной регистрации
        boolean duplicateRegistration = authService.register(username, "anotherpass");
        assertFalse(duplicateRegistration, "Duplicate registration should fail");

        // Успешный логин
        boolean successfulLogin = authService.login(username, password);
        assertTrue(successfulLogin, "Login with correct credentials should succeed");

        // Неуспешный логин с неправильным паролем
        boolean wrongPasswordLogin = authService.login(username, "wrongpassword");
        assertFalse(wrongPasswordLogin, "Login with wrong password should fail");

        // Логин несуществующего пользователя
        boolean nonExistentLogin = authService.login("nonexistent", password);
        assertFalse(nonExistentLogin, "Login of non-existent user should fail");
    }

    @Test
    void testMultipleUsers() {
        // Создание нескольких пользователей
        authService.register("user1", "pass1");
        authService.register("user2", "pass2");
        authService.register("user3", "pass3");

        // Проверка аутентификации для каждого пользователя
        assertTrue(authService.login("user1", "pass1"));
        assertTrue(authService.login("user2", "pass2"));
        assertTrue(authService.login("user3", "pass3"));

        // Проверка, что пользователи не пересекаются
        assertFalse(authService.login("user1", "pass2"));
        assertFalse(authService.login("user2", "pass3"));
        assertFalse(authService.login("user3", "pass1"));
    }

    @Test
    void testUserWithSpecialCharacters() {
        String username = "user@domain.com";
        String password = "p@ssw0rd!";

        boolean registrationResult = authService.register(username, password);
        assertTrue(registrationResult);

        boolean loginResult = authService.login(username, password);
        assertTrue(loginResult);
    }
}