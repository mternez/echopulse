package fr.mternez.echopulse.server.resource.model;

import java.util.Set;
import java.util.UUID;

public record ServerMemberDetailsResource(
        ServerMemberUserDetailsResource user,
        Set<RoleDetailsResource> roles
) {
    public record ServerMemberUserDetailsResource(UUID id, String username, String displayName) {}
}
