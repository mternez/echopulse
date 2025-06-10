package fr.mternez.echopulse.core.server.application.service;

import fr.mternez.echopulse.core.common.domain.error.ChannelNotFound;
import fr.mternez.echopulse.core.common.domain.error.PersistenceException;
import fr.mternez.echopulse.core.common.domain.error.ServerNotFound;
import fr.mternez.echopulse.core.common.domain.model.Channel;
import fr.mternez.echopulse.core.common.event.ChannelCreated;
import fr.mternez.echopulse.core.common.event.ChannelDeleted;
import fr.mternez.echopulse.core.server.application.command.CreateChannelCmd;
import fr.mternez.echopulse.core.server.application.command.DeleteChannelCmd;
import fr.mternez.echopulse.core.server.port.in.ChannelCommandService;
import fr.mternez.echopulse.core.server.port.out.ChannelRepository;
import fr.mternez.echopulse.core.server.port.out.EventPublisher;
import fr.mternez.echopulse.core.server.port.out.ServerRepository;

public class ChannelCommandServiceImpl implements ChannelCommandService {

    private final ChannelRepository channelRepository;
    private final ServerRepository serverRepository;
    private final EventPublisher eventPublisher;

    public ChannelCommandServiceImpl(
            final ChannelRepository channelRepository,
            final ServerRepository serverRepository,
            final EventPublisher eventPublisher
    ) {
        this.channelRepository = channelRepository;
        this.serverRepository = serverRepository;
        this.eventPublisher = eventPublisher;
    }

    @Override
    public Channel execute(CreateChannelCmd cmd) throws PersistenceException, ServerNotFound {

        if (!this.serverRepository.existsById(cmd.serverId())) {
            throw new ServerNotFound(cmd.serverId());
        }

        // Generate and save new channel
        final Channel savedChannel = this.channelRepository.persistNew(new Channel(cmd.serverId(), cmd.name()));

        // Create and publish creation event
        this.eventPublisher.publish(new ChannelCreated(savedChannel.getId(), cmd.serverId(), savedChannel.getName()));

        return savedChannel;
    }

    @Override
    public void execute(DeleteChannelCmd cmd) throws ChannelNotFound, PersistenceException {

        // Find channel
        final Channel channel = this.channelRepository.findById(cmd.channelId())
                .orElseThrow(() -> new ChannelNotFound(cmd.channelId()));

        // Delete channel
        try {
            this.channelRepository.delete(channel);
            // Publish deletion event
            this.eventPublisher.publish(new ChannelDeleted(channel.getId(), channel.getServerId(), channel.getName()));
        } catch (final Exception e) {
            throw new PersistenceException(Channel.class, e);
        }
    }
}
