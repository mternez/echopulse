package fr.mternez.echopulse.core.chat.application;

import fr.mternez.echopulse.core.chat.domain.PostMessage;
import fr.mternez.echopulse.core.chat.port.out.MessagePublisher;
import fr.mternez.echopulse.core.chat.port.out.PostEventPublisher;
import fr.mternez.echopulse.core.common.domain.model.ChannelId;
import fr.mternez.echopulse.core.common.event.PostSent;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Instant;

import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class PostEventHandlerImplTest {

    // Mock: simulate dependencies
    @Mock
    private MessagePublisher messagePublisher;
    @Mock private PostEventPublisher eventPublisher;

    // System under test
    private PostEventHandlerImpl postEventHandler;

    @BeforeEach
    void setUp() {
        postEventHandler = new PostEventHandlerImpl(messagePublisher, eventPublisher);
    }

    @Test
    void should_publish_PostSent_event_when_handling_message() {
        // Arrange
        ChannelId channelId = new ChannelId();
        PostMessage postMessage = new PostMessage("alice", channelId, "hello", Instant.now());

        // Act
        postEventHandler.handleMessage(postMessage);

        // Assert
        verify(eventPublisher).publish(argThat(event -> event.postMessage().equals(postMessage)));
    }

    @Test
    void should_publish_message_when_handling_PostSent_event() {
        // Arrange
        ChannelId channelId = new ChannelId();
        PostMessage postMessage = new PostMessage("bob", channelId, "yo", Instant.now());
        PostSent event = new PostSent(postMessage);

        // Act
        postEventHandler.handleEvent(event);

        // Assert
        verify(messagePublisher).publish(postMessage);
    }
}
