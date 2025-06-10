package fr.mternez.echopulse.core.server.port.out;

import fr.mternez.echopulse.core.common.domain.model.Channel;
import fr.mternez.echopulse.core.common.domain.model.ChannelId;
import fr.mternez.echopulse.core.common.domain.model.ServerId;
import fr.mternez.echopulse.core.common.domain.error.PersistenceException;

import java.util.Optional;
import java.util.Set;

public interface ChannelRepository {

    Channel persistNew(Channel channel) throws PersistenceException;

    void delete(Channel channel);

    Optional<Channel> findById(ChannelId channelId);

    void deleteAllByServerId(ServerId serverId);
}
