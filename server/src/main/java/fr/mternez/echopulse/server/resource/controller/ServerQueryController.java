package fr.mternez.echopulse.server.resource.controller;

import fr.mternez.echopulse.core.common.domain.model.User;
import fr.mternez.echopulse.server.data.projection.ChannelDetails;
import fr.mternez.echopulse.server.data.projection.ServerDetails;
import fr.mternez.echopulse.server.data.projection.ServerMemberDetails;
import fr.mternez.echopulse.server.data.repository.ChannelEntityRepository;
import fr.mternez.echopulse.server.data.repository.MembershipEntityRepository;
import fr.mternez.echopulse.server.data.repository.ServerEntityRepository;
import fr.mternez.echopulse.server.resource.authentication.AuthenticatedUser;
import fr.mternez.echopulse.server.resource.mapper.ResourceMapper;
import fr.mternez.echopulse.server.resource.model.ChannelDetailsResource;
import fr.mternez.echopulse.server.resource.model.ServerDetailsResource;
import fr.mternez.echopulse.server.resource.model.ServerMemberDetailsResource;
import jakarta.transaction.Transactional;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@RestController
@Transactional
@RequestMapping(path = "/api/servers")
public class ServerQueryController {

    private final ServerEntityRepository serverEntityRepository;
    private final MembershipEntityRepository membershipEntityRepository;
    private final ChannelEntityRepository channelEntityRepository;
    private final ResourceMapper resourceMapper;

    ServerQueryController(
            final ServerEntityRepository serverEntityRepository,
            final MembershipEntityRepository membershipEntityRepository,
            final ChannelEntityRepository channelEntityRepository,
            final ResourceMapper resourceMapper
    ) {
        this.serverEntityRepository = serverEntityRepository;
        this.membershipEntityRepository = membershipEntityRepository;
        this.channelEntityRepository = channelEntityRepository;
        this.resourceMapper = resourceMapper;
    }


    @GetMapping("{serverId}")
    public ResponseEntity<ServerDetailsResource> getServerDetails(@AuthenticatedUser User user, @PathVariable UUID serverId) {

        return this.serverEntityRepository
                .findById(serverId, ServerDetails.class)
                .map(this.resourceMapper::toResource)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("{serverId}/channels")
    public Set<ChannelDetailsResource> getChannels(@AuthenticatedUser User user, @PathVariable UUID serverId) {

        return this.channelEntityRepository.findAllByServerId(serverId, ChannelDetails.class)
                .stream()
                .map(this.resourceMapper::toResource)
                .collect(Collectors.toSet());
    }

    @GetMapping("{serverId}/members")
    public Set<ServerMemberDetailsResource> getMembers(@AuthenticatedUser User user, @PathVariable UUID serverId) {

        return this.membershipEntityRepository.findAllByServerId(serverId, ServerMemberDetails.class)
                .stream()
                .map(this.resourceMapper::toResource)
                .collect(Collectors.toSet());
    }
}
