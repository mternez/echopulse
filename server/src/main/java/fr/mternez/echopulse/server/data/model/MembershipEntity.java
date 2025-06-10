package fr.mternez.echopulse.server.data.model;

import jakarta.persistence.*;

import java.util.Set;
import java.util.UUID;

@Entity
@Table(name = "memberships")
public class MembershipEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id")
    private UserEntity user;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "server_id")
    private ServerEntity server;

    @ManyToMany(fetch = FetchType.EAGER)
    private Set<RoleEntity> roles;

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public UserEntity getUser() {
        return user;
    }

    public void setUser(final UserEntity userId) {
        this.user = userId;
    }

    public ServerEntity getServer() {
        return server;
    }

    public void setServer(ServerEntity server) {
        this.server = server;
    }

    public Set<RoleEntity> getRoles() {
        return roles;
    }

    public void setRoles(final Set<RoleEntity> roles) {
        this.roles = roles;
    }
}
