package fr.mternez.echopulse.core.server.application.service;

import fr.mternez.echopulse.core.common.domain.error.PersistenceException;
import fr.mternez.echopulse.core.common.domain.error.RoleNotFound;
import fr.mternez.echopulse.core.common.domain.error.ServerNotFound;
import fr.mternez.echopulse.core.common.domain.model.Permission;
import fr.mternez.echopulse.core.common.domain.model.Role;
import fr.mternez.echopulse.core.common.domain.model.Server;
import fr.mternez.echopulse.core.common.domain.model.ServerId;
import fr.mternez.echopulse.core.server.application.command.CreateRoleCmd;
import fr.mternez.echopulse.core.server.application.command.DeleteRoleCmd;
import fr.mternez.echopulse.core.server.port.in.RoleCommandService;
import fr.mternez.echopulse.core.server.port.out.ServerRepository;

public class RoleCommandServiceImpl implements RoleCommandService {

    private final ServerRepository serverRepository;

    public RoleCommandServiceImpl(
            final ServerRepository serverRepository
    ) {
        this.serverRepository = serverRepository;
    }

    @Override
    public Role execute(final CreateRoleCmd cmd) throws ServerNotFound, PersistenceException {

        final ServerId serverId = cmd.serverId();
        final String roleName = cmd.name();

        final Server server = this.serverRepository.findById(serverId)
                .orElseThrow(() -> new ServerNotFound(serverId));

        final Role role = new Role(roleName, cmd.permissions().toArray(new Permission[0]));

        server.addRole(role);

        this.serverRepository.update(server);

        return role;
    }

    @Override
    public void execute(DeleteRoleCmd cmd) throws ServerNotFound, RoleNotFound, PersistenceException {

        final ServerId serverId = cmd.serverId();
        final String roleName = cmd.name();

        final Server server = this.serverRepository.findById(serverId)
                .orElseThrow(() -> new ServerNotFound(serverId));

        server.removeRole(roleName);

        this.serverRepository.update(server);
    }
}
