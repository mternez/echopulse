package fr.mternez.echopulse.server.data.adapter;

import fr.mternez.echopulse.core.common.domain.model.Channel;
import fr.mternez.echopulse.core.common.domain.model.ChannelId;
import fr.mternez.echopulse.core.common.domain.model.Server;
import fr.mternez.echopulse.core.common.domain.model.ServerId;
import fr.mternez.echopulse.core.common.domain.error.PersistenceException;
import fr.mternez.echopulse.core.server.port.out.ChannelRepository;
import fr.mternez.echopulse.server.data.mapper.DomainModelMapper;
import fr.mternez.echopulse.server.data.model.ChannelEntity;
import fr.mternez.echopulse.server.data.model.ServerEntity;
import fr.mternez.echopulse.server.data.repository.ChannelEntityRepository;
import fr.mternez.echopulse.server.data.repository.ServerEntityRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Repository
public class ChannelRepositoryJpaAdapter implements ChannelRepository {

    private final ServerEntityRepository serverEntityRepository;
    private final ChannelEntityRepository channelEntityRepository;
    private final DomainModelMapper domainModelMapper;

    ChannelRepositoryJpaAdapter(
            final ServerEntityRepository serverEntityRepository,
            final ChannelEntityRepository channelEntityRepository,
            final DomainModelMapper domainModelMapper
    ) {
        this.serverEntityRepository = serverEntityRepository;
        this.channelEntityRepository = channelEntityRepository;
        this.domainModelMapper = domainModelMapper;
    }

    @Override
    @Transactional
    public Channel persistNew(Channel channel) throws PersistenceException {

        final ServerEntity serverEntity = this.serverEntityRepository.findById(channel.getServerId().getValue())
                .orElseThrow(() -> new PersistenceException(Server.class, new IllegalArgumentException("Server not found")));

        final ChannelEntity entity = new ChannelEntity();
        entity.setId(channel.getId().getValue());
        entity.setName(channel.getName());
        entity.setServer(serverEntity);

        final ChannelEntity savedEntity = this.channelEntityRepository.save(entity);

        if(savedEntity == null) {
            throw new PersistenceException(Channel.class, new RuntimeException("Failed to save channel"));
        }

        return this.domainModelMapper.toDomainModel(savedEntity);
    }

    @Override
    @Transactional
    public void delete(Channel channel) {

        this.channelEntityRepository.deleteById(channel.getId().getValue());
    }

    @Override
    @Transactional
    public Optional<Channel> findById(ChannelId channelId) {

        return this.channelEntityRepository
                .findById(channelId.getValue())
                .map(this.domainModelMapper::toDomainModel);
    }

    @Override
    @Transactional
    public void deleteAllByServerId(final ServerId serverId) {

        final Set<ChannelEntity> channels = this.channelEntityRepository
                .findChannelEntitiesByServerId(serverId.getValue());

        for(var channel : channels) {
            channel.setServer(null);
        }

        this.channelEntityRepository.deleteAll(channels);
    }
}
