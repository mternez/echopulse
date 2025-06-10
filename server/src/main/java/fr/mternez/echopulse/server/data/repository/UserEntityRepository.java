package fr.mternez.echopulse.server.data.repository;

import fr.mternez.echopulse.server.data.model.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface UserEntityRepository extends JpaRepository<UserEntity, UUID> {

    boolean existsUserEntityByUsername(final String username);
    UserEntity findUserEntityByUsername(final String username);
    <T> Optional<T> findByUsername(final String username, Class<T> clazz);
}
