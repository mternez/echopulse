package fr.mternez.echopulse.server.data.repository;

import fr.mternez.echopulse.server.data.model.RoleEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.Set;
import java.util.UUID;

@Repository
public interface RoleEntityRepository extends JpaRepository<RoleEntity, UUID> {

    Optional<RoleEntity> findRoleEntityByServerIdAndName(UUID serverId, String name);
    Set<RoleEntity> findRoleEntitiesByServerIdAndNameIsIn(UUID serverId, Set<String> names);
}
