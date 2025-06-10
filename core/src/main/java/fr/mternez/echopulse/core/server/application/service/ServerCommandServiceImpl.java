package fr.mternez.echopulse.core.server.application.service;

import fr.mternez.echopulse.core.common.domain.error.ServerNotFound;
import fr.mternez.echopulse.core.common.domain.error.UserNotFound;
import fr.mternez.echopulse.core.common.domain.model.*;
import fr.mternez.echopulse.core.server.application.command.CreateServerCmd;
import fr.mternez.echopulse.core.server.application.command.DeleteServerCmd;
import fr.mternez.echopulse.core.common.domain.error.PersistenceException;
import fr.mternez.echopulse.core.server.port.in.ServerCommandService;
import fr.mternez.echopulse.core.server.port.out.ChannelRepository;
import fr.mternez.echopulse.core.server.port.out.MembershipRepository;
import fr.mternez.echopulse.core.server.port.out.ServerRepository;
import fr.mternez.echopulse.core.server.port.out.UserRepository;

import java.util.logging.Logger;

public class ServerCommandServiceImpl implements ServerCommandService {

    private static final Logger logger = Logger.getLogger(ServerCommandServiceImpl.class.getName());

    private final UserRepository userRepository;
    private final ServerRepository serverRepository;
    private final MembershipRepository membershipRepository;
    private final ChannelRepository channelRepository;

    public ServerCommandServiceImpl(
            final UserRepository userRepository,
            final ServerRepository serverRepository,
            final MembershipRepository membershipRepository,
            final ChannelRepository channelRepository
    ) {
        this.userRepository = userRepository;
        this.serverRepository = serverRepository;
        this.membershipRepository = membershipRepository;
        this.channelRepository = channelRepository;
    }

    @Override
    public Server execute(final CreateServerCmd cmd) throws UserNotFound, PersistenceException {

        // Fetch server owner
        final User owner = this.userRepository.findById(cmd.invocationSource().user().getId())
                .orElseThrow(() -> new UserNotFound(cmd.invocationSource().user().getId()));

        // Create server
        final Server server = new Server(new ServerId(), cmd.name(), owner);

        // Create owner role
        final Role ownerRole = new Role(
                "OWNER",
                Permission.MANAGE_CHANNELS,
                Permission.MANAGE_MEMBERS,
                Permission.MANAGE_ROLES,
                Permission.DELETE_SERVER
        );
        server.addRole(ownerRole);

        // Create default role
        final Role defaultRole = new Role(
                "USER"
        );
        server.addRole(defaultRole);
        server.setDefaultRole(defaultRole);

        final Server savedServer = this.serverRepository.persistNew(server);

        // Add owner membership
        owner.addMembership(new Membership(savedServer.getId(), owner.getId(), ownerRole, defaultRole));

        // Save owner
        this.userRepository.update(owner);

        return savedServer;
    }

    @Override
    public void execute(final DeleteServerCmd cmd) throws ServerNotFound, PersistenceException {

        // Delete all memberships
        this.membershipRepository.deleteAllByServerId(cmd.serverId());

        // Delete all channels
        this.channelRepository.deleteAllByServerId(cmd.serverId());

        // Delete server
        this.serverRepository.deleteById(cmd.serverId());
    }
}
