package fr.mternez.echopulse.core.server.port.out;

import fr.mternez.echopulse.core.common.domain.model.User;
import fr.mternez.echopulse.core.common.domain.model.UserId;
import fr.mternez.echopulse.core.common.domain.error.PersistenceException;

import java.util.Optional;

public interface UserRepository {

    User persistNew(User user) throws PersistenceException;

    User update(User user) throws PersistenceException;

    Optional<User> findById(UserId userId);
}
