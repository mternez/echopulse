package fr.mternez.echopulse.core.common.domain.model;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

public class Membership {

    private final ServerId serverId;

    private final UserId userId;

    private final Set<Role> roles = new HashSet<>();

    public Membership(final ServerId serverId, final UserId userId, final Role ...initialRoles) {
        if(serverId == null) {
            throw new IllegalArgumentException("Server id cannot be null");
        }
        if(userId == null) {
            throw new IllegalArgumentException("User id cannot be null");
        }
        if(initialRoles == null || initialRoles.length == 0) {
            throw new IllegalArgumentException("An initial role must be provided");
        }
        this.serverId = serverId;
        this.userId = userId;
        for(Role role : initialRoles) {
            this.roles.add(role);
        }
    }

    public Membership(final ServerId serverId, final UserId userId, final Set<Role> initialRoles) {
        if(serverId == null) {
            throw new IllegalArgumentException("Server id cannot be null");
        }
        if(userId == null) {
            throw new IllegalArgumentException("User id cannot be null");
        }
        if(initialRoles == null || initialRoles.isEmpty()) {
            throw new IllegalArgumentException("An initial role must be provided");
        }
        this.serverId = serverId;
        this.userId = userId;
        this.roles.addAll(initialRoles);
    }

    public ServerId getServerId() {
        return this.serverId;
    }

    public UserId getUserId() {
        return this.userId;
    }

    public Set<Role> getRoles() {
        return Collections.unmodifiableSet(this.roles);
    }

    public void addRole(final Role role) {
        final boolean alreadyExists = this.roles.stream().anyMatch(r -> r.getName().equals(role.getName()));
        if(alreadyExists) {
           throw new IllegalArgumentException("Role already exists");
        }
        this.roles.add(role);
    }

    public void removeRole(final Role role) {
        this.roles.remove(role);
    }
}
