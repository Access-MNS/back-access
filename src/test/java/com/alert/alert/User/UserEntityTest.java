package com.alert.alert.User;

import com.alert.alert.entity.User;

import com.alert.alert.repository.UserRepository;
import com.alert.alert.service.impl.user.UserServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;
import org.mockito.Mockito;


import java.util.Optional;

import static org.hibernate.validator.internal.util.Contracts.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@ExtendWith(MockitoExtension.class)
public class UserEntityTest {

    @InjectMocks
    private UserServiceImpl userService;

    @Mock
    private UserRepository userRepository;

    @Test
    public void testGetEmployeeById() {

        long userId = 1L;
        User mockUser = new User(userId, "Doe", "John", "john.doe@example.com", "$2a$10$2uJunru8qka3BMpEe9JmvudRkTE/9Ihf/Ag0QopF.iyxJG3ss1UjO", null, null, false, null);

        // Mock the behavior of the repository to return the mock user
        Mockito.when(userRepository.findById(userId)).thenReturn(Optional.of(mockUser));

        // Act
        User result = userService.getUser(userId);

        // Assert
        assertNotNull(result);
        assertEquals(userId, result.getId());
        assertEquals("John", result.getFirstName());
        assertEquals("Doe", result.getLastName());
        assertEquals("john.doe@example.com", result.getMail());
    }
}
