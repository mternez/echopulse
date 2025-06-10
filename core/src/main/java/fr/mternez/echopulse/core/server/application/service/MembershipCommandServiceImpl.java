package fr.mternez.echopulse.core.server.application.service;

import fr.mternez.echopulse.core.common.domain.error.*;
import fr.mternez.echopulse.core.common.domain.model.*;
import fr.mternez.echopulse.core.common.event.UserJoinedServer;
import fr.mternez.echopulse.core.common.event.UserLeftServer;
import fr.mternez.echopulse.core.common.event.UserRole;
import fr.mternez.echopulse.core.server.application.command.CreateMembershipCmd;
import fr.mternez.echopulse.core.server.application.command.DeleteMembershipCmd;
import fr.mternez.echopulse.core.server.port.in.MembershipCommandService;
import fr.mternez.echopulse.core.server.port.out.EventPublisher;
import fr.mternez.echopulse.core.server.port.out.MembershipRepository;
import fr.mternez.echopulse.core.server.port.out.ServerRepository;
import fr.mternez.echopulse.core.server.port.out.UserRepository;

import java.util.Set;
import java.util.logging.Logger;
import java.util.stream.Collectors;

public class MembershipCommandServiceImpl implements MembershipCommandService {

    private static final Logger logger = Logger.getLogger(MembershipCommandServiceImpl.class.getName());

    private final UserRepository userRepository;
    private final MembershipRepository membershipRepository;
    private final ServerRepository serverRepository;
    private final EventPublisher eventPublisher;

    public MembershipCommandServiceImpl(
            final UserRepository userRepository,
            final MembershipRepository membershipRepository,
            final ServerRepository serverRepository,
            final EventPublisher eventPublisher
    ) {
        this.userRepository = userRepository;
        this.membershipRepository = membershipRepository;
        this.serverRepository = serverRepository;
        this.eventPublisher = eventPublisher;
    }

    @Override
    public Membership execute(final CreateMembershipCmd cmd) throws UserNotFound, ServerNotFound, RoleNotFound, PersistenceException {

        final ServerId serverId = cmd.serverId();
        final UserId userId = cmd.targetUserId();

        // Find user
        final User user = this.userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFound(userId));

        // Find server default role
        final Role defaultServerRole = this.serverRepository.findDefaultRole(serverId).orElseThrow(
                () -> new RoleNotFound(serverId)
        );

        // Create and save membership
        final Membership savedMembership = this.membershipRepository.persistNew(new Membership(serverId, user.getId(), Set.of(defaultServerRole)));

        // Add membership to user and save
        user.addMembership(savedMembership);
        this.userRepository.persistNew(user);

        // Publish event
        this.eventPublisher.publish(new UserJoinedServer(
                userId,
                serverId,
                user.getUsername(),
                user.getDisplayName(),
                savedMembership.getRoles().stream().map(r -> new UserRole(r.getName(), r.getDescription(), r.getColor(), r.getPermissions())).collect(Collectors.toSet())
        ));

        return savedMembership;
    }

    @Override
    public void execute(final DeleteMembershipCmd cmd) throws MembershipNotFound, PersistenceException {

        final ServerId serverId = cmd.serverId();
        final UserId userId = cmd.userId();

        // Delete membership
        this.membershipRepository.deleteByServerIdUserId(cmd.serverId(), cmd.userId());

        // Publish event
        this.eventPublisher.publish(new UserLeftServer(
                userId,
                serverId
        ));
    }
}
