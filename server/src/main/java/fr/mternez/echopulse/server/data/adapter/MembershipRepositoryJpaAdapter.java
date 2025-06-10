package fr.mternez.echopulse.server.data.adapter;

import fr.mternez.echopulse.core.common.domain.model.*;
import fr.mternez.echopulse.core.common.domain.error.PersistenceException;
import fr.mternez.echopulse.core.server.port.out.MembershipRepository;
import fr.mternez.echopulse.server.data.mapper.DomainModelMapper;
import fr.mternez.echopulse.server.data.model.MembershipEntity;
import fr.mternez.echopulse.server.data.model.RoleEntity;
import fr.mternez.echopulse.server.data.model.ServerEntity;
import fr.mternez.echopulse.server.data.model.UserEntity;
import fr.mternez.echopulse.server.data.repository.MembershipEntityRepository;
import fr.mternez.echopulse.server.data.repository.RoleEntityRepository;
import fr.mternez.echopulse.server.data.repository.ServerEntityRepository;
import fr.mternez.echopulse.server.data.repository.UserEntityRepository;
import org.springframework.stereotype.Component;

import java.util.Set;
import java.util.stream.Collectors;

@Component
public class MembershipRepositoryJpaAdapter implements MembershipRepository {

    private final MembershipEntityRepository membershipEntityRepository;
    private final UserEntityRepository userEntityRepository;
    private final ServerEntityRepository serverEntityRepository;
    private final DomainModelMapper domainModelMapper;

    public MembershipRepositoryJpaAdapter(
            final MembershipEntityRepository membershipEntityRepository,
            final UserEntityRepository userEntityRepository,
            final RoleEntityRepository roleEntityRepository,
            final ServerEntityRepository serverEntityRepository,
            final DomainModelMapper domainModelMapper
    ) {
        this.membershipEntityRepository = membershipEntityRepository;
        this.userEntityRepository = userEntityRepository;
        this.serverEntityRepository = serverEntityRepository;
        this.domainModelMapper = domainModelMapper;
    }

    @Override
    public Membership persistNew(final Membership membership) throws PersistenceException {

        final UserEntity user = this.userEntityRepository.findById(membership.getUserId().getValue())
                .orElseThrow(() -> new PersistenceException(User.class, new IllegalArgumentException("User not found")));

        final ServerEntity server = this.serverEntityRepository.findById(membership.getServerId().getValue())
                .orElseThrow(() -> new PersistenceException(User.class, new IllegalArgumentException("Server not found")));

        final MembershipEntity entity = new MembershipEntity();
        entity.setUser(user);
        entity.setServer(server);
        final Set<String> roleNames = membership.getRoles().stream().map(Role::getName).collect(Collectors.toSet());
        final Set<RoleEntity> roles = server.getRoles().stream().filter(r -> roleNames.contains(r.getName())).collect(Collectors.toSet());
        entity.setRoles(roles);

        final MembershipEntity savedEntity = this.membershipEntityRepository.save(entity);

        if(savedEntity == null) {
            throw new PersistenceException(Membership.class, new IllegalArgumentException("Membership could not be saved"));
        }

        user.getMemberships().add(savedEntity);

        if(this.userEntityRepository.save(user) == null) {
            throw new PersistenceException(User.class, new IllegalArgumentException("User could not be saved during membership creation"));
        }

        return this.domainModelMapper.toDomainModel(savedEntity);
    }

    @Override
    public void deleteByServerIdUserId(final ServerId serverId, final UserId userId) throws PersistenceException {

        final Set<MembershipEntity> memberships = this.membershipEntityRepository
                .findAllByServerIdAndUser_Id(serverId.getValue(), userId.getValue(), MembershipEntity.class);

        for(var membership : memberships) {
            membership.setServer(null);
            membership.getUser().getMemberships().remove(membership);
            membership.setUser(null);
        }

        this.membershipEntityRepository.deleteAll(memberships);
    }

    @Override
    public void deleteAllByServerId(final ServerId serverId) {

        final Set<MembershipEntity> memberships = this.membershipEntityRepository
                .findMembershipEntitiesByServerId(serverId.getValue());

        for(var membership : memberships) {
            membership.setServer(null);
            membership.getUser().getMemberships().remove(membership);
            membership.setUser(null);
        }

        this.membershipEntityRepository.deleteAll(memberships);
    }
}
