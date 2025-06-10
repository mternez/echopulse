package fr.mternez.echopulse.core.server.application.service;

import fr.mternez.echopulse.core.common.application.InvocationSource;
import fr.mternez.echopulse.core.common.domain.error.UserNotFound;
import fr.mternez.echopulse.core.common.domain.model.*;
import fr.mternez.echopulse.core.server.application.command.CreateServerCmd;
import fr.mternez.echopulse.core.server.application.command.DeleteServerCmd;
import fr.mternez.echopulse.core.server.port.out.ChannelRepository;
import fr.mternez.echopulse.core.server.port.out.MembershipRepository;
import fr.mternez.echopulse.core.server.port.out.ServerRepository;
import fr.mternez.echopulse.core.server.port.out.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Instant;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;


@ExtendWith(MockitoExtension.class)
class ServerCommandServiceImplTest {

    // Mock: simulate dependencies
    @Mock
    private UserRepository userRepository;
    @Mock private ServerRepository serverRepository;
    @Mock private MembershipRepository membershipRepository;
    @Mock private ChannelRepository channelRepository;

    // System under test
    private ServerCommandServiceImpl serverCommandService;

    @BeforeEach
    void setUp() {
        serverCommandService = new ServerCommandServiceImpl(
                userRepository,
                serverRepository,
                membershipRepository,
                channelRepository
        );
    }

    @Test
    void should_create_server_successfully() throws Exception {
        // Arrange
        UserId ownerId = new UserId();
        User owner = new User(ownerId, "admin");

        CreateServerCmd cmd = new CreateServerCmd(
                new InvocationSource(owner, Instant.now()),
                "MyServer"
        );
        Server server = new Server(new ServerId(), "MyServer", owner);
        server.addRole(new Role("OWNER"));
        server.addRole(new Role("USER"));

        // Mock
        when(userRepository.findById(ownerId)).thenReturn(Optional.of(owner));
        when(serverRepository.persistNew(any(Server.class))).thenReturn(server);

        // Act
        Server result = serverCommandService.execute(cmd);

        // Assert
        assertNotNull(result);
        assertEquals("MyServer", result.getName());
        assertEquals(owner, result.getOwner());
        assertTrue(result.hasRoles("OWNER", "USER"));
        verify(userRepository).update(owner);
    }

    @Test
    void should_throw_UserNotFound_when_owner_does_not_exist() {
        // Arrange
        UserId unknownId = new UserId();
        User owner = new User(unknownId, "ghost");
        CreateServerCmd cmd = new CreateServerCmd(
                new InvocationSource(owner, Instant.now()),
                "GhostServer"
        );

        // Mock
        when(userRepository.findById(unknownId)).thenReturn(Optional.empty());

        // Act + Assert
        assertThrows(UserNotFound.class, () -> serverCommandService.execute(cmd));
    }

    @Test
    void should_delete_server_and_associated_resources() throws Exception {
        // Arrange
        ServerId serverId = new ServerId();
        DeleteServerCmd cmd = new DeleteServerCmd(
                new InvocationSource(null, Instant.now()),
                serverId
        );

        // Act
        serverCommandService.execute(cmd);

        // Assert
        verify(membershipRepository).deleteAllByServerId(serverId);
        verify(channelRepository).deleteAllByServerId(serverId);
        verify(serverRepository).deleteById(serverId);
    }
}
