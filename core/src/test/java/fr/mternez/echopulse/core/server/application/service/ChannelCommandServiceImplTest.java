package fr.mternez.echopulse.core.server.application.service;

import fr.mternez.echopulse.core.common.application.InvocationSource;
import fr.mternez.echopulse.core.common.domain.error.ChannelNotFound;
import fr.mternez.echopulse.core.common.domain.error.ServerNotFound;
import fr.mternez.echopulse.core.common.domain.model.Channel;
import fr.mternez.echopulse.core.common.domain.model.ChannelId;
import fr.mternez.echopulse.core.common.domain.model.ServerId;
import fr.mternez.echopulse.core.common.event.ChannelCreated;
import fr.mternez.echopulse.core.common.event.ChannelDeleted;
import fr.mternez.echopulse.core.server.application.command.CreateChannelCmd;
import fr.mternez.echopulse.core.server.application.command.DeleteChannelCmd;
import fr.mternez.echopulse.core.server.port.out.ChannelRepository;
import fr.mternez.echopulse.core.server.port.out.EventPublisher;
import fr.mternez.echopulse.core.server.port.out.ServerRepository;
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
class ChannelCommandServiceImplTest {

    // Mock: simulate dependencies
    @Mock
    private ChannelRepository channelRepository;

    @Mock
    private ServerRepository serverRepository;

    @Mock
    private EventPublisher eventPublisher;

    // Arrange: system under test
    private ChannelCommandServiceImpl channelCommandService;

    @BeforeEach
    void setUp() {
        this.channelCommandService = new ChannelCommandServiceImpl(
                channelRepository, serverRepository, eventPublisher
        );
    }

    @Test
    void should_create_channel_when_server_exists() throws Exception {
        // Arrange
        ServerId serverId = new ServerId();
        String channelName = "general";
        CreateChannelCmd cmd = new CreateChannelCmd(
                new InvocationSource(null, Instant.now()), serverId, channelName
        );
        Channel channel = new Channel(serverId, channelName);

        // Mock
        when(serverRepository.existsById(serverId)).thenReturn(true);
        when(channelRepository.persistNew(any(Channel.class))).thenReturn(channel);

        // Act
        Channel result = channelCommandService.execute(cmd);

        // Assert
        assertNotNull(result);
        assertEquals(channelName, result.getName());
        verify(eventPublisher).publish(any(ChannelCreated.class));
        verify(channelRepository).persistNew(any(Channel.class));
    }

    @Test
    void should_throw_ServerNotFound_when_server_does_not_exist() {
        // Arrange
        ServerId serverId = new ServerId();
        CreateChannelCmd cmd = new CreateChannelCmd(
                new InvocationSource(null, Instant.now()), serverId, "test"
        );

        // Mock
        when(serverRepository.existsById(serverId)).thenReturn(false);

        // Act + Assert
        assertThrows(ServerNotFound.class, () -> channelCommandService.execute(cmd));
    }

    @Test
    void should_delete_channel_successfully() throws Exception {
        // Arrange
        ChannelId channelId = new ChannelId();
        ServerId serverId = new ServerId();
        Channel channel = new Channel(channelId, serverId, "general");
        DeleteChannelCmd cmd = new DeleteChannelCmd(
                new InvocationSource(null, Instant.now()), serverId, channelId
        );

        // Mock
        when(channelRepository.findById(channelId)).thenReturn(Optional.of(channel));

        // Act
        channelCommandService.execute(cmd);

        // Assert
        verify(channelRepository).delete(channel);
        verify(eventPublisher).publish(any(ChannelDeleted.class));
    }

    @Test
    void should_throw_ChannelNotFound_when_channel_does_not_exist() {
        // Arrange
        ChannelId channelId = new ChannelId();
        ServerId serverId = new ServerId();
        DeleteChannelCmd cmd = new DeleteChannelCmd(
                new InvocationSource(null, Instant.now()), serverId, channelId
        );

        // Mock
        when(channelRepository.findById(channelId)).thenReturn(Optional.empty());

        // Act + Assert
        assertThrows(ChannelNotFound.class, () -> channelCommandService.execute(cmd));
    }
}
