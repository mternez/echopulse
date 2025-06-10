package fr.mternez.echopulse.core.chat.application;

import fr.mternez.echopulse.core.chat.domain.UserJoinedMessage;
import fr.mternez.echopulse.core.chat.domain.UserLeftMessage;
import fr.mternez.echopulse.core.chat.port.out.MessagePublisher;
import fr.mternez.echopulse.core.common.domain.model.ServerId;
import fr.mternez.echopulse.core.common.domain.model.UserId;
import fr.mternez.echopulse.core.common.event.UserJoinedServer;
import fr.mternez.echopulse.core.common.event.UserLeftServer;
import fr.mternez.echopulse.core.common.event.UserRole;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Set;

import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class UserEventHandlerImplTest {

    // Mock: simulate dependency
    @Mock
    private MessagePublisher messagePublisher;

    // System under test
    private UserEventHandlerImpl userEventHandler;

    @BeforeEach
    void setUp() {
        userEventHandler = new UserEventHandlerImpl(messagePublisher);
    }

    @Test
    void should_publish_UserJoinedMessage_when_handling_UserJoinedServer_event() {
        // Arrange
        UserId userId = new UserId();
        ServerId serverId = new ServerId();
        Set<UserRole> roles = Set.of(new UserRole("MEMBER", "", "", Set.of()));
        UserJoinedServer event = new UserJoinedServer(userId, serverId, "alice", "Alice", roles);

        // Act
        userEventHandler.handleEvent(event);

        // Assert
        verify(messagePublisher).publish((UserJoinedMessage) argThat(msg ->
                msg instanceof UserJoinedMessage &&
                        ((UserJoinedMessage) msg).event().equals(event)
        ));
    }

    @Test
    void should_publish_UserLeftMessage_when_handling_UserLeftServer_event() {
        // Arrange
        UserId userId = new UserId();
        ServerId serverId = new ServerId();
        UserLeftServer event = new UserLeftServer(userId, serverId);

        // Act
        userEventHandler.handleEvent(event);

        // Assert
        verify(messagePublisher).publish((UserLeftMessage) argThat(msg ->
                msg instanceof UserLeftMessage &&
                        ((UserLeftMessage) msg).event().equals(event)
        ));
    }
}
