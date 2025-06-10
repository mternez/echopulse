package fr.mternez.echopulse.core.server.port.in;

import fr.mternez.echopulse.core.common.domain.error.PersistenceException;
import fr.mternez.echopulse.core.server.application.command.CreateUserCmd;
import fr.mternez.echopulse.core.common.domain.model.User;

public interface UserCommandService {

    /**
     * Creates a user.
     * @param cmd
     * @return
     */
    User execute(CreateUserCmd cmd) throws PersistenceException;
}
