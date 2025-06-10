package fr.mternez.echopulse.core.common.domain.model;

import fr.mternez.echopulse.core.common.domain.error.RoleNotFound;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

public class Server {

    private final ServerId id;

    private String name;

    private User owner;

    private Role defaultRole;

    private final Set<Role> roles = new HashSet<>();

    public Server(
            final ServerId id,
            final String name,
            final User owner
    ) {
        this.id = id;
        this.setName(name);
        this.setOwner(owner);
    }

    public ServerId getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    private void setName(final String name) {
        if(name == null || name.isEmpty()) {
            throw new IllegalArgumentException("Server name cannot be null or empty");
        }
        this.name = name;
    }

    public User getOwner() {
        return owner;
    }

    public void setOwner(User owner) {
        if(owner == null || owner.getId() == null) {
            throw new IllegalArgumentException("Server owner cannot be null or empty");
        }
        this.owner = owner;
    }

    public Role getDefaultRole() {
        return this.defaultRole;
    }

    public void setDefaultRole(final Role defaultRole) {
        this.defaultRole = defaultRole;
    }

    public Set<Role> getRoles() {
        return Collections.unmodifiableSet(this.roles);
    }

    public void addRole(final Role role) {
        this.roles.add(role);
    }

    public void removeRole(final Role role) {
        this.removeRole(role.getName());
    }

    public void removeRole(final String roleName) {
        if(this.roles.stream().noneMatch(r -> r.getName().equals(roleName))) {
            throw new RoleNotFound(this.id, roleName);
        }
        this.roles.removeIf(r -> r.getName().equals(roleName));
    }

    public boolean hasRoles(final String ...roleNames) {
        if(roleNames == null || roleNames.length == 0) {
            return false;
        }
        for(final String roleName : roleNames) {
            if(this.roles.stream().noneMatch(r -> r.getName().equals(roleName))) {
                return false;
            }
        }
        return true;
    }
}