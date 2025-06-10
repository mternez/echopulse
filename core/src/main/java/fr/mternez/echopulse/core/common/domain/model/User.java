package fr.mternez.echopulse.core.common.domain.model;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

public class User {

    private final UserId id;

    private String username;

    private String displayName;

    private final Set<Membership> memberships = new HashSet<>();

    public User(final UserId id, final String username) {
        this.id = id;
        this.setUsername(username);
        this.setDisplayName(username);
    }

    public User(final UserId id, final String username, final String displayName) {
        this.id = id;
        this.setUsername(username);
        this.setDisplayName(displayName);
    }

    public UserId getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(final String username) {
        if(username == null || username.isEmpty()) {
            throw new IllegalArgumentException("Username cannot be null or empty");
        }
        this.username = username;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        if(displayName == null || displayName.isEmpty()) {
            throw new IllegalArgumentException("Display name cannot be null or empty");
        }
        this.displayName = displayName;
    }

    public Set<Membership> getMemberships() {
        return Collections.unmodifiableSet(this.memberships);
    }

    public Membership getMembership(final ServerId serverId) {
        return this.memberships.stream()
                .filter(m -> m.getServerId().equals(serverId))
                .findFirst()
                .orElse(null);
    }

    public void addMemberships(final Membership ...membership) {
        Arrays.stream(membership).forEach(this.memberships::add);
    }

    public void addMembership(final Membership membership) {

        final boolean alreadyExists = this.memberships
                .stream()
                .anyMatch(m -> m.getServerId().equals(membership.getServerId()));
        if(alreadyExists) {
            throw new IllegalArgumentException("Membership already exists");
        }
        this.memberships.add(membership);
    }

    public void removeMembership(final Membership membership) {
        this.memberships.remove(membership);
    }

    public void removeMembership(final ServerId serverId) {
        this.memberships.removeIf(m -> m.getServerId().equals(serverId));
    }

    public boolean isMemberOf(final ServerId serverId) {
        return this.memberships.stream().anyMatch(m -> m.getServerId().equals(serverId));
    }

    public boolean hasPermission(final Permission permission) {
        return this.memberships.stream().anyMatch(
                (membership) -> membership.getRoles().stream().anyMatch(
                        (role) -> role.getPermissions().stream().anyMatch(permission::equals)
                )
        );
    }

    public boolean hasRole(final Role role) {
        return this.memberships.stream().anyMatch(
                (membership) -> membership.getRoles().stream().anyMatch(
                        r -> r.getName().equals(role.getName())
                )
        );
    }
}
