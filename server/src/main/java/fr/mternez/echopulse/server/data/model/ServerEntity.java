package fr.mternez.echopulse.server.data.model;

import jakarta.persistence.*;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Entity
@Table(name = "servers")
public class ServerEntity implements Serializable {

    @Id
    private UUID id;

    private String name;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "owner_id")
    private UserEntity owner;

    @OneToOne(fetch = FetchType.EAGER)
    private RoleEntity defaultRole;

    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, mappedBy = "server")
    private Set<RoleEntity> roles = new HashSet<>();

    public void setId(UUID id) {
        this.id = id;
    }

    public UUID getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public UserEntity getOwner() {
        return owner;
    }

    public void setOwner(UserEntity owner) {
        this.owner = owner;
    }

    public RoleEntity getDefaultRole() {
        return defaultRole;
    }

    public void setDefaultRole(RoleEntity defaultRole) {
        this.defaultRole = defaultRole;
        this.defaultRole.setServer(this);
    }

    public Set<RoleEntity> getRoles() {
        return roles;
    }

    public void addRole(RoleEntity role) {
        this.roles.add(role);
        role.setServer(this);
    }

    public void removeRole(RoleEntity role) {
        this.roles.remove(role);
        role.setServer(null);
    }

    private void setRoles(Set<RoleEntity> roles) {
        this.roles = roles;
    }
}
