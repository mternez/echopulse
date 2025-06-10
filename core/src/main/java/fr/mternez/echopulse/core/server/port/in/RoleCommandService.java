package fr.mternez.echopulse.core.server.port.in;

import fr.mternez.echopulse.core.common.domain.error.PersistenceException;
import fr.mternez.echopulse.core.common.domain.error.RoleNotFound;
import fr.mternez.echopulse.core.common.domain.error.ServerNotFound;
import fr.mternez.echopulse.core.common.domain.model.Role;
import fr.mternez.echopulse.core.server.application.command.CreateRoleCmd;
import fr.mternez.echopulse.core.server.application.command.DeleteRoleCmd;

public interface RoleCommandService {

    Role execute(CreateRoleCmd cmd) throws ServerNotFound, PersistenceException;

    void execute(DeleteRoleCmd cmd) throws ServerNotFound, RoleNotFound, PersistenceException;
}
