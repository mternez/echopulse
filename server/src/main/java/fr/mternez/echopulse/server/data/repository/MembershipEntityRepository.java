package fr.mternez.echopulse.server.data.repository;

import fr.mternez.echopulse.server.data.model.MembershipEntity;
import fr.mternez.echopulse.server.data.model.MembershipPK;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Set;
import java.util.UUID;

@Repository
public interface MembershipEntityRepository extends JpaRepository<MembershipEntity, MembershipPK> {

    Set<MembershipEntity> findMembershipEntitiesByServerId(UUID serverId);

    <T> Set<T> findAllByServerId(UUID serverId, Class<T> clazz);

    <T> Set<T> findAllByServerIdAndUser_Id(UUID serverId, UUID userId, Class<T> clazz);

    <T> T findByServerIdAndUser_Id(UUID serverId, UUID userId, Class<T> clazz);

    void deleteMembershipEntityByServerIdAndUser_Id(UUID serverId, UUID userId);

    void deleteMembershipEntitiesByServerId(UUID serverId);
}
