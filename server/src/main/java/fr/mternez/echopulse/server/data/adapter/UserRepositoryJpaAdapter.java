package fr.mternez.echopulse.server.data.adapter;

import fr.mternez.echopulse.core.common.domain.model.Membership;
import fr.mternez.echopulse.core.common.domain.model.Role;
import fr.mternez.echopulse.core.common.domain.model.User;
import fr.mternez.echopulse.core.common.domain.model.UserId;
import fr.mternez.echopulse.core.common.domain.error.PersistenceException;
import fr.mternez.echopulse.core.server.port.out.UserRepository;
import fr.mternez.echopulse.server.data.mapper.DomainModelMapper;
import fr.mternez.echopulse.server.data.model.MembershipEntity;
import fr.mternez.echopulse.server.data.model.RoleEntity;
import fr.mternez.echopulse.server.data.model.ServerEntity;
import fr.mternez.echopulse.server.data.model.UserEntity;
import fr.mternez.echopulse.server.data.repository.MembershipEntityRepository;
import fr.mternez.echopulse.server.data.repository.ServerEntityRepository;
import fr.mternez.echopulse.server.data.repository.UserEntityRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Component
public class UserRepositoryJpaAdapter implements UserRepository {

    private final UserEntityRepository userEntityRepository;
    private final MembershipEntityRepository membershipEntityRepository;
    private final ServerEntityRepository serverEntityRepository;
    private final DomainModelMapper domainModelMapper;

    UserRepositoryJpaAdapter(
            final UserEntityRepository userEntityRepository,
            final MembershipEntityRepository membershipEntityRepository,
            final ServerEntityRepository serverEntityRepository,
            final DomainModelMapper domainModelMapper
    ) {
        this.userEntityRepository = userEntityRepository;
        this.membershipEntityRepository = membershipEntityRepository;
        this.serverEntityRepository = serverEntityRepository;
        this.domainModelMapper = domainModelMapper;
    }

    @Override
    @Transactional
    public User persistNew(final User user) throws PersistenceException {

        final UserEntity userEntity = new UserEntity();
        userEntity.setId(user.getId().getValue());
        userEntity.setUsername(user.getUsername());
        userEntity.setDisplayName(user.getDisplayName());
        try {
            final UserEntity savedUserEntity = this.userEntityRepository.save(userEntity);
            return this.domainModelMapper.toDomainModel(savedUserEntity);
        } catch(Exception e) {
            throw new PersistenceException(UserEntity.class, e);
        }
    }

    @Override
    @Transactional
    public User update(final User user) throws PersistenceException {

        final UserEntity userEntity = this.userEntityRepository
                .findById(user.getId().getValue())
                .orElseThrow(() -> new PersistenceException(User.class, new IllegalArgumentException("User not found")));

        userEntity.setUsername(user.getUsername());
        userEntity.setDisplayName(user.getDisplayName());

        // Remove memberships
        userEntity
                .getMemberships()
                .removeIf(
                        membershipEntity ->
                user.getMemberships().stream().noneMatch(
                        membership -> membership.getServerId().getValue() == membershipEntity.getServer().getId()
                )
        );

        // Add memberships
        final Set<Membership> newMemberships = user.getMemberships().stream()
                .filter(membership -> userEntity.getMemberships().stream()
                        .noneMatch(membershipEntity ->  membership.getServerId().getValue() == membershipEntity.getServer().getId())
                ).collect(Collectors.toSet());
        for(var newMembership : newMemberships) {
            final MembershipEntity newMembershipEntity = this.createMembership(userEntity, newMembership);
            userEntity.getMemberships().add(newMembershipEntity);
        }

        return this.domainModelMapper.toDomainModel(this.userEntityRepository.save(userEntity));
    }

    private MembershipEntity createMembership(final UserEntity user, Membership membership) throws PersistenceException {

        final ServerEntity server = this.serverEntityRepository.findById(membership.getServerId().getValue())
                .orElseThrow(() -> new PersistenceException(User.class, new IllegalArgumentException("Server not found")));

        final Set<String> roleNames = membership.getRoles().stream().map(Role::getName).collect(Collectors.toSet());
        final MembershipEntity entity = new MembershipEntity();
        entity.setUser(user);
        entity.setServer(server);
        final Set<RoleEntity> roles = server.getRoles().stream().filter(r -> roleNames.contains(r.getName())).collect(Collectors.toSet());
        roles.add(server.getDefaultRole());
        entity.setRoles(roles);
        return this.membershipEntityRepository.save(entity);
    }

    @Override
    @Transactional
    public Optional<User> findById(final UserId userId) {

        return this.userEntityRepository
                .findById(userId.getValue())
                .map(this.domainModelMapper::toDomainModel);
    }
}
