package fr.mternez.echopulse.core.server.application.service;

import fr.mternez.echopulse.core.common.application.InvocationSource;
import fr.mternez.echopulse.core.common.domain.error.PermissionDenied;
import fr.mternez.echopulse.core.common.domain.model.*;
import fr.mternez.echopulse.core.server.application.command.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Instant;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

@ExtendWith(MockitoExtension.class)
class CommandAuthorizationServiceImplTest {

    private CommandAuthorizationServiceImpl authorizationService;

    @BeforeEach
    void setUp() {
        this.authorizationService = new CommandAuthorizationServiceImpl();
    }

    @Test
    void should_authorize_when_user_is_member_and_has_permission() {
        // Arrange
        ServerId serverId = new ServerId();
        UserId userId = new UserId();
        Role role = new Role("MOD", Permission.MANAGE_CHANNELS);
        Membership membership = new Membership(serverId, userId, role);
        User user = new User(userId, "john");
        user.addMembership(membership);
        CreateChannelCmd cmd = new CreateChannelCmd(new InvocationSource(user, Instant.now()), serverId, "test");

        // Act + Assert
        assertDoesNotThrow(() -> authorizationService.authorize(cmd));
    }

    @Test
    void should_throw_when_user_not_member() {
        // Arrange
        ServerId serverId = new ServerId();
        UserId userId = new UserId();
        Role role = new Role("MOD", Permission.MANAGE_CHANNELS);
        Membership otherMembership = new Membership(new ServerId(), userId, role); // different server
        User user = new User(userId, "ghost");
        user.addMembership(otherMembership);
        DeleteChannelCmd cmd = new DeleteChannelCmd(new InvocationSource(user, Instant.now()), serverId, new ChannelId());

        // Act + Assert
        assertThrows(PermissionDenied.class, () -> authorizationService.authorize(cmd));
    }

    @Test
    void should_throw_when_user_lacks_required_permission() {
        // Arrange
        ServerId serverId = new ServerId();
        UserId userId = new UserId();
        Role role = new Role("USER", Permission.MANAGE_MEMBERS); // wrong permission
        Membership membership = new Membership(serverId, userId, role);
        User user = new User(userId, "alice");
        user.addMembership(membership);
        DeleteRoleCmd cmd = new DeleteRoleCmd(new InvocationSource(user, Instant.now()), serverId, "role");

        // Act + Assert
        assertThrows(PermissionDenied.class, () -> authorizationService.authorize(cmd));
    }

    @Test
    void should_authorize_all_open_commands() {
        // Arrange
        InvocationSource src = new InvocationSource(null, Instant.now());

        // Act + Assert
        assertDoesNotThrow(() -> authorizationService.authorize(new CreateUserCmd(src, "user")));
        assertDoesNotThrow(() -> authorizationService.authorize(new CreateServerCmd(src, "srv")));
    }
}
