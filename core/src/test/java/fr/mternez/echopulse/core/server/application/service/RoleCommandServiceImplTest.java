package fr.mternez.echopulse.core.server.application.service;

import fr.mternez.echopulse.core.common.application.InvocationSource;
import fr.mternez.echopulse.core.common.domain.error.RoleNotFound;
import fr.mternez.echopulse.core.common.domain.error.ServerNotFound;
import fr.mternez.echopulse.core.common.domain.model.*;
import fr.mternez.echopulse.core.server.application.command.CreateRoleCmd;
import fr.mternez.echopulse.core.server.application.command.DeleteRoleCmd;
import fr.mternez.echopulse.core.server.port.out.ServerRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Instant;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class RoleCommandServiceImplTest {

    // Mock: simulate ServerRepository
    @Mock
    private ServerRepository serverRepository;

    // Arrange: system under test
    private RoleCommandServiceImpl roleCommandService;

    @BeforeEach
    void setUp() {
        this.roleCommandService = new RoleCommandServiceImpl(serverRepository);
    }

    @Test
    void should_create_role_successfully() throws Exception {
        // Arrange
        ServerId serverId = new ServerId();
        User owner = new User(new UserId(), "admin");
        Server server = new Server(serverId, "MyServer", owner);
        CreateRoleCmd cmd = new CreateRoleCmd(
                new InvocationSource(owner, Instant.now()),
                serverId,
                "MODERATOR",
                Set.of(Permission.MANAGE_MEMBERS)
        );

        // Mock
        when(serverRepository.findById(serverId)).thenReturn(Optional.of(server));
        when(serverRepository.update(any(Server.class))).thenReturn(server);

        // Act
        Role result = roleCommandService.execute(cmd);

        // Assert
        assertNotNull(result);
        assertEquals("MODERATOR", result.getName());
        assertTrue(result.getPermissions().contains(Permission.MANAGE_MEMBERS));
        verify(serverRepository).update(server);
    }

    @Test
    void should_throw_ServerNotFound_when_server_does_not_exist() {
        // Arrange
        ServerId serverId = new ServerId();
        CreateRoleCmd cmd = new CreateRoleCmd(
                new InvocationSource(null, Instant.now()),
                serverId,
                "ADMIN",
                Set.of(Permission.MANAGE_ROLES)
        );

        // Mock
        when(serverRepository.findById(serverId)).thenReturn(Optional.empty());

        // Act + Assert
        assertThrows(ServerNotFound.class, () -> roleCommandService.execute(cmd));
    }

    @Test
    void should_delete_role_successfully() throws Exception {
        // Arrange
        ServerId serverId = new ServerId();
        User owner = new User(new UserId(), "owner");
        Server server = new Server(serverId, "Server", owner);
        Role role = new Role("TO_DELETE");
        server.addRole(role);
        DeleteRoleCmd cmd = new DeleteRoleCmd(
                new InvocationSource(owner, Instant.now()),
                serverId,
                "TO_DELETE"
        );

        // Mock
        when(serverRepository.findById(serverId)).thenReturn(Optional.of(server));
        when(serverRepository.update(server)).thenReturn(server);

        // Act
        roleCommandService.execute(cmd);

        // Assert
        assertFalse(server.getRoles().contains(role));
        verify(serverRepository).update(server);
    }

    @Test
    void should_throw_RoleNotFound_when_deleting_nonexistent_role() {
        // Arrange
        ServerId serverId = new ServerId();
        User owner = new User(new UserId(), "owner");
        Server server = new Server(serverId, "Server", owner);
        DeleteRoleCmd cmd = new DeleteRoleCmd(
                new InvocationSource(owner, Instant.now()),
                serverId,
                "MISSING"
        );

        // Mock
        when(serverRepository.findById(serverId)).thenReturn(Optional.of(server));

        // Act + Assert
        assertThrows(RoleNotFound.class, () -> roleCommandService.execute(cmd));
    }
}
