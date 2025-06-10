package fr.mternez.echopulse.core.chat.application;

import fr.mternez.echopulse.core.chat.domain.ChannelCreatedMessage;
import fr.mternez.echopulse.core.chat.domain.ChannelDeletedMessage;
import fr.mternez.echopulse.core.chat.port.out.MessagePublisher;
import fr.mternez.echopulse.core.common.domain.model.ChannelId;
import fr.mternez.echopulse.core.common.domain.model.ServerId;
import fr.mternez.echopulse.core.common.event.ChannelCreated;
import fr.mternez.echopulse.core.common.event.ChannelDeleted;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class ChannelEventHandlerImplTest {

    // Mock: simulate dependency
    @Mock
    private MessagePublisher messagePublisher;

    // System under test
    private ChannelEventHandlerImpl channelEventHandler;

    @BeforeEach
    void setUp() {
        channelEventHandler = new ChannelEventHandlerImpl(messagePublisher);
    }

    @Test
    void should_publish_ChannelCreatedMessage_when_handling_ChannelCreated_event() {
        // Arrange
        ChannelId channelId = new ChannelId();
        ServerId serverId = new ServerId();
        ChannelCreated event = new ChannelCreated(channelId, serverId, "general");

        // Act
        channelEventHandler.handleEvent(event);

        // Assert
        verify(messagePublisher).publish((ChannelCreatedMessage) argThat(msg ->
                msg instanceof ChannelCreatedMessage &&
                        ((ChannelCreatedMessage) msg).event().equals(event)
        ));
    }

    @Test
    void should_publish_ChannelDeletedMessage_when_handling_ChannelDeleted_event() {
        // Arrange
        ChannelId channelId = new ChannelId();
        ServerId serverId = new ServerId();
        ChannelDeleted event = new ChannelDeleted(channelId, serverId, "random");

        // Act
        channelEventHandler.handleEvent(event);

        // Assert
        verify(messagePublisher).publish((ChannelDeletedMessage) argThat(msg ->
                msg instanceof ChannelDeletedMessage &&
                        ((ChannelDeletedMessage) msg).event().equals(event)
        ));
    }
}
