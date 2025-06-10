package fr.mternez.echopulse.core.server.application.service;

import fr.mternez.echopulse.core.common.application.InvocationSource;
import fr.mternez.echopulse.core.common.domain.error.PersistenceException;
import fr.mternez.echopulse.core.common.domain.model.User;
import fr.mternez.echopulse.core.common.domain.model.UserId;
import fr.mternez.echopulse.core.server.application.command.CreateUserCmd;
import fr.mternez.echopulse.core.server.port.out.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Instant;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserCommandServiceImplTest {

    // Mock: simulate UserRepository
    @Mock
    private UserRepository userRepository;

    // Arrange: system under test
    private UserCommandServiceImpl userCommandService;

    @BeforeEach
    void setUp() {
        this.userCommandService = new UserCommandServiceImpl(userRepository);
    }

    @Test
    void should_create_user_successfully() throws PersistenceException {
        // Arrange
        UserId userId = new UserId(UUID.randomUUID());
        User mockUser = new User(userId, "testuser");
        CreateUserCmd cmd = new CreateUserCmd(
                new InvocationSource(mockUser, Instant.now()),
                "testuser"
        );

        // Mock
        when(userRepository.persistNew(any(User.class))).thenReturn(mockUser);

        // Act
        User result = userCommandService.execute(cmd);

        // Assert
        assertNotNull(result);
        assertEquals("testuser", result.getUsername());
        verify(userRepository).persistNew(any(User.class));
    }
}
