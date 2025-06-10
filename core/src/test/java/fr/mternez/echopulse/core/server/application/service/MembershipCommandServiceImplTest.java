package fr.mternez.echopulse.core.server.application.service;

import fr.mternez.echopulse.core.common.application.InvocationSource;
import fr.mternez.echopulse.core.common.domain.error.RoleNotFound;
import fr.mternez.echopulse.core.common.domain.error.UserNotFound;
import fr.mternez.echopulse.core.common.domain.model.*;
import fr.mternez.echopulse.core.common.event.UserJoinedServer;
import fr.mternez.echopulse.core.common.event.UserLeftServer;
import fr.mternez.echopulse.core.server.application.command.CreateMembershipCmd;
import fr.mternez.echopulse.core.server.application.command.DeleteMembershipCmd;
import fr.mternez.echopulse.core.server.port.out.EventPublisher;
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
class MembershipCommandServiceImplTest {

    // Mock: simulate dependencies
    @Mock
    private UserRepository userRepository;
    @Mock private MembershipRepository membershipRepository;
    @Mock private ServerRepository serverRepository;
    @Mock private EventPublisher eventPublisher;

    // Arrange: system under test
    private MembershipCommandServiceImpl membershipCommandService;

    @BeforeEach
    void setUp() {
        membershipCommandService = new MembershipCommandServiceImpl(
                userRepository, membershipRepository, serverRepository, eventPublisher
        );
    }

    @Test
    void should_create_membership_successfully() throws Exception {
        // Arrange
        ServerId serverId = new ServerId();
        UserId userId = new UserId();
        Role defaultRole = new Role("USER");
        User user = new User(userId, "john");
        Membership membership = new Membership(serverId, userId, defaultRole);
        CreateMembershipCmd cmd = new CreateMembershipCmd(
                new InvocationSource(user, Instant.now()), serverId, userId
        );

        // Mock
        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(serverRepository.findDefaultRole(serverId)).thenReturn(Optional.of(defaultRole));
        when(membershipRepository.persistNew(any(Membership.class))).thenReturn(membership);
        when(userRepository.persistNew(any(User.class))).thenReturn(user);

        // Act
        Membership result = membershipCommandService.execute(cmd);

        // Assert
        assertNotNull(result);
        assertEquals(serverId, result.getServerId());
        assertEquals(userId, result.getUserId());
        verify(eventPublisher).publish(any(UserJoinedServer.class));
    }

    @Test
    void should_throw_UserNotFound_when_user_does_not_exist() {
        // Arrange
        ServerId serverId = new ServerId();
        UserId userId = new UserId();
        CreateMembershipCmd cmd = new CreateMembershipCmd(
                new InvocationSource(null, Instant.now()), serverId, userId
        );

        // Mock
        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        // Act + Assert
        assertThrows(UserNotFound.class, () -> membershipCommandService.execute(cmd));
    }

    @Test
    void should_throw_RoleNotFound_when_default_role_missing() {
        // Arrange
        ServerId serverId = new ServerId();
        UserId userId = new UserId();
        User user = new User(userId, "alice");
        CreateMembershipCmd cmd = new CreateMembershipCmd(
                new InvocationSource(user, Instant.now()), serverId, userId
        );

        // Mock
        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(serverRepository.findDefaultRole(serverId)).thenReturn(Optional.empty());

        // Act + Assert
        assertThrows(RoleNotFound.class, () -> membershipCommandService.execute(cmd));
    }

    @Test
    void should_delete_membership_and_publish_event() throws Exception {
        // Arrange
        ServerId serverId = new ServerId();
        UserId userId = new UserId();
        DeleteMembershipCmd cmd = new DeleteMembershipCmd(
                new InvocationSource(null, Instant.now()), serverId, userId
        );

        // Act
        membershipCommandService.execute(cmd);

        // Assert
        verify(membershipRepository).deleteByServerIdUserId(serverId, userId);
        verify(eventPublisher).publish(any(UserLeftServer.class));
    }
}
