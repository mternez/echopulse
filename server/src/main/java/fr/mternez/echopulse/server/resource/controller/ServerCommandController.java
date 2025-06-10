package fr.mternez.echopulse.server.resource.controller;

import fr.mternez.echopulse.core.common.application.InvocationSource;
import fr.mternez.echopulse.core.common.domain.error.PersistenceException;
import fr.mternez.echopulse.core.common.domain.model.ChannelId;
import fr.mternez.echopulse.core.common.domain.model.ServerId;
import fr.mternez.echopulse.core.common.domain.model.User;
import fr.mternez.echopulse.core.server.application.command.*;
import fr.mternez.echopulse.core.server.port.in.ChannelCommandService;
import fr.mternez.echopulse.core.server.port.in.CommandAuthorizationService;
import fr.mternez.echopulse.core.server.port.in.MembershipCommandService;
import fr.mternez.echopulse.core.server.port.in.ServerCommandService;
import fr.mternez.echopulse.server.data.projection.MembershipSummary;
import fr.mternez.echopulse.server.data.repository.MembershipEntityRepository;
import fr.mternez.echopulse.server.resource.authentication.AuthenticatedUser;
import fr.mternez.echopulse.server.resource.mapper.ResourceMapper;
import fr.mternez.echopulse.server.resource.model.*;
import jakarta.transaction.Transactional;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;
import java.util.UUID;

@RestController
@Transactional
@RequestMapping(path = "/api/servers")
public class ServerCommandController {

    private final ServerCommandService serverCommandService;
    private final ChannelCommandService channelCommandService;
    private final MembershipCommandService membershipCommandService;
    private final CommandAuthorizationService commandAuthorizationService;
    private final MembershipEntityRepository membershipEntityRepository;
    private final ResourceMapper resourceMapper;

    ServerCommandController(
            final ServerCommandService serverCommandService,
            final ChannelCommandService channelCommandService,
            final MembershipCommandService membershipCommandService,
            final CommandAuthorizationService commandAuthorizationService,
            final MembershipEntityRepository membershipEntityRepository,
            final ResourceMapper resourceMapper
    ) {
        this.serverCommandService = serverCommandService;
        this.channelCommandService = channelCommandService;
        this.membershipCommandService = membershipCommandService;
        this.commandAuthorizationService = commandAuthorizationService;
        this.membershipEntityRepository = membershipEntityRepository;
        this.resourceMapper = resourceMapper;
    }

    @PostMapping
    public ServerDetailsResource create(@AuthenticatedUser User user, @RequestBody ServerCreationResource newServer) throws PersistenceException {

        final CreateServerCmd cmd = new CreateServerCmd(this.getInvocationSource(user), newServer.name());
        this.commandAuthorizationService.authorize(cmd);
        return this.resourceMapper.toResource(this.serverCommandService
                .execute(new CreateServerCmd(this.getInvocationSource(user), newServer.name())));
    }

    @PostMapping("{serverId}/channels")
    public ChannelDetailsResource createChannel(@AuthenticatedUser User user, @PathVariable UUID serverId, @RequestBody ChannelCreationResource newChannel) throws PersistenceException {

        final CreateChannelCmd cmd = new CreateChannelCmd(this.getInvocationSource(user), new ServerId(serverId), newChannel.name());
        this.commandAuthorizationService.authorize(cmd);
        return this.resourceMapper.toResource(this.channelCommandService.execute(cmd));
    }

    @PostMapping("{serverId}/members")
    public MembershipSummaryResource joinServer(@AuthenticatedUser User user, @PathVariable UUID serverId) throws PersistenceException {

        final CreateMembershipCmd cmd = new CreateMembershipCmd(this.getInvocationSource(user), new ServerId(serverId), user.getId());
        this.membershipCommandService.execute(cmd);
        return this.resourceMapper.toResource(this.membershipEntityRepository.findByServerIdAndUser_Id(serverId, user.getId().getValue(), MembershipSummary.class));
    }

    @DeleteMapping("{serverId}")
    public void deleteServer(@AuthenticatedUser User user, @PathVariable UUID serverId) throws PersistenceException {

        final DeleteServerCmd cmd = new DeleteServerCmd(this.getInvocationSource(user), new ServerId(serverId));
        this.commandAuthorizationService.authorize(cmd);
        this.serverCommandService.execute(cmd);
    }

    @DeleteMapping("{serverId}/channels/{channelId}")
    public void deleteChannel(@AuthenticatedUser User user, @PathVariable UUID serverId, @PathVariable UUID channelId) throws PersistenceException {

        final DeleteChannelCmd cmd = new DeleteChannelCmd(this.getInvocationSource(user), new ServerId(serverId), new ChannelId(channelId));
        this.commandAuthorizationService.authorize(cmd);
        this.channelCommandService.execute(cmd);
    }

    @DeleteMapping("{serverId}/members")
    public void leaveServer(@AuthenticatedUser User user, @PathVariable UUID serverId) throws PersistenceException {

        final DeleteMembershipCmd cmd = new DeleteMembershipCmd(this.getInvocationSource(user), new ServerId(serverId), user.getId());

        this.membershipCommandService.execute(cmd);
    }

    private InvocationSource getInvocationSource(final User user) {
        return new InvocationSource(user, Instant.now());
    }
}
