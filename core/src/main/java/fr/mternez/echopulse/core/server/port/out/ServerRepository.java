package fr.mternez.echopulse.core.server.port.out;

import fr.mternez.echopulse.core.common.domain.model.Role;
import fr.mternez.echopulse.core.common.domain.model.Server;
import fr.mternez.echopulse.core.common.domain.model.ServerId;
import fr.mternez.echopulse.core.common.domain.error.PersistenceException;

import java.util.Optional;

public interface ServerRepository {

    Server persistNew(Server server) throws PersistenceException;

    Server update(Server server) throws PersistenceException;

    Optional<Server> findById(ServerId serverId);

    Optional<Role> findDefaultRole(ServerId serverId);

    boolean existsById(ServerId serverId);

    void deleteById(ServerId serverId);
}
