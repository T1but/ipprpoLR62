package com.example.auth;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuthServiceTest {

    @Mock
    private UserRepository userRepository;

    private AuthService authService;

    @BeforeEach
    void setUp() {
        authService = new AuthService(userRepository);
    }

    @Test
    void testSuccessfulLogin() {
        // Arrange
        String username = "testuser";
        String password = "password123";
        User mockUser = new User(username, password, true);

        when(userRepository.findByUsername(username)).thenReturn(mockUser);

        // Act
        boolean result = authService.login(username, password);

        // Assert
        assertTrue(result);
        verify(userRepository).findByUsername(username);
    }

    @Test
    void testLoginWithWrongPassword() {
        // Arrange
        String username = "testuser";
        String correctPassword = "password123";
        String wrongPassword = "wrongpassword";
        User mockUser = new User(username, correctPassword, true);

        when(userRepository.findByUsername(username)).thenReturn(mockUser);

        // Act
        boolean result = authService.login(username, wrongPassword);

        // Assert
        assertFalse(result);
        verify(userRepository).findByUsername(username);
    }

    @Test
    void testLoginWithNonExistentUser() {
        // Arrange
        String username = "nonexistent";
        String password = "password123";

        when(userRepository.findByUsername(username)).thenReturn(null);

        // Act
        boolean result = authService.login(username, password);

        // Assert
        assertFalse(result);
        verify(userRepository).findByUsername(username);
    }

    @Test
    void testLoginWithInactiveUser() {
        // Arrange
        String username = "inactiveuser";
        String password = "password123";
        User mockUser = new User(username, password, false);

        when(userRepository.findByUsername(username)).thenReturn(mockUser);

        // Act
        boolean result = authService.login(username, password);

        // Assert
        assertFalse(result);
        verify(userRepository).findByUsername(username);
    }

    @Test
    void testLoginWithNullUsername() {
        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> {
            authService.login(null, "password");
        });
    }

    @Test
    void testLoginWithNullPassword() {
        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> {
            authService.login("username", null);
        });
    }

    @Test
    void testSuccessfulRegistration() {
        // Arrange
        String username = "newuser";
        String password = "password123";

        when(userRepository.existsByUsername(username)).thenReturn(false);

        // Act
        boolean result = authService.register(username, password);

        // Assert
        assertTrue(result);
        verify(userRepository).existsByUsername(username);
        verify(userRepository).save(any(User.class));
    }

    @Test
    void testRegistrationWithExistingUser() {
        // Arrange
        String username = "existinguser";
        String password = "password123";

        when(userRepository.existsByUsername(username)).thenReturn(true);

        // Act
        boolean result = authService.register(username, password);

        // Assert
        assertFalse(result);
        verify(userRepository).existsByUsername(username);
        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    void testRegistrationWithInvalidData() {
        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> {
            authService.register("", "password");
        });

        assertThrows(IllegalArgumentException.class, () -> {
            authService.register("username", null);
        });
    }
}