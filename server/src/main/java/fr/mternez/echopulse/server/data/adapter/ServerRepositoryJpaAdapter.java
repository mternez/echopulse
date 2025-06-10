package fr.mternez.echopulse.server.data.adapter;

import fr.mternez.echopulse.core.common.domain.model.Role;
import fr.mternez.echopulse.core.common.domain.model.Server;
import fr.mternez.echopulse.core.common.domain.model.ServerId;
import fr.mternez.echopulse.core.common.domain.model.User;
import fr.mternez.echopulse.core.common.domain.error.PersistenceException;
import fr.mternez.echopulse.core.server.port.out.ServerRepository;
import fr.mternez.echopulse.server.data.mapper.DomainModelMapper;
import fr.mternez.echopulse.server.data.model.RoleEntity;
import fr.mternez.echopulse.server.data.model.ServerEntity;
import fr.mternez.echopulse.server.data.model.UserEntity;
import fr.mternez.echopulse.server.data.projection.ServerDefaultRole;
import fr.mternez.echopulse.server.data.repository.RoleEntityRepository;
import fr.mternez.echopulse.server.data.repository.ServerEntityRepository;
import fr.mternez.echopulse.server.data.repository.UserEntityRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Component;

import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Component
public class ServerRepositoryJpaAdapter implements ServerRepository {

    private final ServerEntityRepository serverEntityRepository;
    private final UserEntityRepository userEntityRepository;
    private final RoleEntityRepository roleEntityRepository;
    private final DomainModelMapper domainModelMapper;

    ServerRepositoryJpaAdapter(
            final ServerEntityRepository serverEntityRepository,
            final UserEntityRepository userEntityRepository,
            final RoleEntityRepository roleEntityRepository,
            final DomainModelMapper domainModelMapper
    ) {
        this.serverEntityRepository = serverEntityRepository;
        this.userEntityRepository = userEntityRepository;
        this.roleEntityRepository = roleEntityRepository;
        this.domainModelMapper = domainModelMapper;
    }

    @Override
    @Transactional
    public Server persistNew(final Server server) throws PersistenceException {

        final UserEntity owner = this.userEntityRepository.findById(server.getOwner().getId().getValue())
                .orElseThrow(() -> new PersistenceException(User.class, new IllegalArgumentException("User not found")));

        final ServerEntity serverEntity = new ServerEntity();
        serverEntity.setId(server.getId().getValue());
        serverEntity.setName(server.getName());
        serverEntity.setOwner(owner);

        final ServerEntity savedServer = this.serverEntityRepository.save(serverEntity);

        final RoleEntity defaultRole = this.roleEntityRepository.save(this.createRoleEntity(savedServer, server.getDefaultRole()));
        savedServer.setDefaultRole(defaultRole);

        server.getRoles().forEach(role -> {
            // Create all roles except for default role
            if(!Objects.equals(role.getName(), savedServer.getDefaultRole().getName())) {
                final RoleEntity roleEntity = this.roleEntityRepository.save(this.createRoleEntity(savedServer, role));
                savedServer.addRole(roleEntity);
            }
        });

        try {
            return this.domainModelMapper.toDomainModel(this.serverEntityRepository.save(savedServer));
        } catch(Exception e) {
            throw new PersistenceException(ServerEntity.class, e);
        }
    }

    @Override
    @Transactional
    public Server update(Server server) throws PersistenceException {

        final UUID serverId = server.getId().getValue();

        final ServerEntity entity = this.serverEntityRepository.findById(serverId)
                .orElseThrow(() -> new PersistenceException(Server.class, new IllegalArgumentException("Server not found")));

        entity.setName(server.getName());
        this.updateOwner(entity, server);
        this.updateDefaultRole(entity, server);
        this.updateRoles(entity, server);

        return this.domainModelMapper.toDomainModel(this.serverEntityRepository.save(entity));
    }

    private void updateOwner(ServerEntity entity, Server model) throws PersistenceException {

        final UUID oldOwnerId = entity.getOwner().getId();
        final UUID newOwnerId = model.getOwner().getId().getValue();

        if(!newOwnerId.equals(oldOwnerId)) {
            final UserEntity owner = this.userEntityRepository.findById(newOwnerId)
                    .orElseThrow(() -> new PersistenceException(User.class, new IllegalArgumentException("User not found")));
            entity.setOwner(owner);
        }
    }

    private void updateDefaultRole(ServerEntity entity, Server model) throws PersistenceException {

        final String oldDefaultRoleName = entity.getDefaultRole().getName();
        final String newDefaultRoleName = model.getDefaultRole().getName();

        if(!newDefaultRoleName.equals(oldDefaultRoleName)) {
            final RoleEntity defaultRole = this.roleEntityRepository.findRoleEntityByServerIdAndName(model.getId().getValue(), newDefaultRoleName)
                    .orElseThrow(() -> new PersistenceException(RoleEntity.class, new IllegalArgumentException("Role not found")));
            entity.setDefaultRole(defaultRole);
        }

    }

    private void updateRoles(ServerEntity entity, Server model) throws PersistenceException {

        final Set<String> oldRoleNames = entity.getRoles().stream().map(RoleEntity::getName).collect(Collectors.toSet());
        final Set<String> newRoleNames = model.getRoles().stream().map(Role::getName).collect(Collectors.toSet());

        // Remove roles
        oldRoleNames.removeIf(oldRole -> !newRoleNames.contains(oldRole));

        // Filter roles not added yet
        final Set<String> newRoles = newRoleNames.stream()
                .filter(newRole -> !oldRoleNames.contains(newRole))
                .collect(Collectors.toSet());

        final Set<RoleEntity> newRoleEntities = this.roleEntityRepository.findRoleEntitiesByServerIdAndNameIsIn(model.getId().getValue(), newRoles);

        if(newRoleEntities.size() != newRoles.size()) {
            throw new PersistenceException(RoleEntity.class, new IllegalArgumentException("Roles not found"));
        }

        entity.getRoles().addAll(newRoleEntities);
    }

    @Override
    @Transactional
    public Optional<Server> findById(ServerId serverId) {
        return this.serverEntityRepository.findById(serverId.getValue()).map(this.domainModelMapper::toDomainModel);
    }

    @Override
    @Transactional
    public boolean existsById(final ServerId serverId) {

        return this.serverEntityRepository.existsById(serverId.getValue());
    }

    @Override
    @Transactional
    public void deleteById(ServerId serverId) {

        this.serverEntityRepository.deleteById(serverId.getValue());
    }
    
    @Override
    @Transactional
    public Optional<Role> findDefaultRole(ServerId serverId) {

        return this.serverEntityRepository
                .findById(serverId.getValue(), ServerDefaultRole.class)
                .map(ServerDefaultRole::getDefaultRole)
                .map(this.domainModelMapper::toDomainModel);
    }

    private RoleEntity createRoleEntity(final ServerEntity server, final Role model) {
        final RoleEntity roleEntity = new RoleEntity();
        roleEntity.setName(model.getName());
        roleEntity.setDescription(model.getDescription());
        roleEntity.setColor(model.getColor());
        roleEntity.setPermissions(model.getPermissions());
        roleEntity.setServer(server);
        return roleEntity;
    }
}
