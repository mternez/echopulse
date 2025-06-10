package fr.mternez.echopulse.server.data.repository;

import fr.mternez.echopulse.server.data.model.ServerEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

@Repository
public interface ServerEntityRepository extends JpaRepository<ServerEntity, UUID> {

    <T> Optional<T> findById(final UUID id, Class<T> type);

    <T> Set<T> findAllByIdIn(Collection<UUID> ids, Class<T> type);
}
