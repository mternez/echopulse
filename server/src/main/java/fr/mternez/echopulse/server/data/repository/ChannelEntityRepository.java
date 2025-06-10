package fr.mternez.echopulse.server.data.repository;

import fr.mternez.echopulse.server.data.model.ChannelEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Set;
import java.util.UUID;

@Repository
public interface ChannelEntityRepository extends JpaRepository<ChannelEntity, UUID> {

    Set<ChannelEntity> findChannelEntitiesByServerId(UUID serverId);

    <T> Set<T> findAllByServerId(UUID serverId, Class<T> clazz);

    void deleteChannelEntitiesByServerId(UUID serverId);
}
