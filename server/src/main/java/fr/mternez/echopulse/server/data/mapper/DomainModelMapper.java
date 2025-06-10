package fr.mternez.echopulse.server.data.mapper;

import fr.mternez.echopulse.core.common.domain.model.*;
import fr.mternez.echopulse.server.data.model.*;
import org.springframework.stereotype.Component;

import java.util.Set;
import java.util.stream.Collectors;

@Component
public class DomainModelMapper {

    public Channel toDomainModel(final ChannelEntity entity) {
        return new Channel(new ChannelId(entity.getId()), new ServerId(entity.getServer().getId()), entity.getName());
    }

    public Role toDomainModel(final RoleEntity entity) {
        final Role role = new Role(entity.getName());
        role.setDescription(entity.getDescription());
        role.setColor(entity.getColor());
        entity.getPermissions().forEach(role::addPermission);
        return role;
    }

    public Membership toDomainModel(final MembershipEntity entity) {
        final Set<Role> roles = entity.getRoles().stream().map(this::toDomainModel).collect(Collectors.toSet());
        return new Membership(new ServerId(entity.getServer().getId()), new UserId(entity.getUser().getId()), roles);
    }

    public Server toDomainModel(final ServerEntity entity) {
        final Server model = new Server(new ServerId(entity.getId()), entity.getName(), this.toDomainModel(entity.getOwner()));
        model.setDefaultRole(this.toDomainModel(entity.getDefaultRole()));
        entity.getRoles().forEach(role -> model.addRole(this.toDomainModel(role)));
        return model;
    }

    public User toDomainModel(final UserEntity entity) {

        final UserId id = new UserId(entity.getId());
        final String username = entity.getUsername();
        final String displayName = entity.getDisplayName();
        final User user = new User(id, username, displayName);
        entity.getMemberships().stream().forEach(
                membershipEntity -> user.addMembership(this.toDomainModel(membershipEntity))
        );
        return user;
    }
}
