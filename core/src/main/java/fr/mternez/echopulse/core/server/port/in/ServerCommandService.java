package fr.mternez.echopulse.core.server.port.in;

import fr.mternez.echopulse.core.common.domain.error.ServerNotFound;
import fr.mternez.echopulse.core.server.application.command.DeleteServerCmd;
import fr.mternez.echopulse.core.common.domain.error.PersistenceException;
import fr.mternez.echopulse.core.server.application.command.CreateServerCmd;
import fr.mternez.echopulse.core.common.domain.error.UserNotFound;
import fr.mternez.echopulse.core.common.domain.model.Server;

public interface ServerCommandService {

    Server execute(CreateServerCmd cmd) throws UserNotFound, PersistenceException;

    void execute(DeleteServerCmd cmd) throws ServerNotFound, PersistenceException;
}
