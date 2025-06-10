package fr.mternez.echopulse.server.resource.model;

import java.util.Set;
import java.util.UUID;

public record ServerDetailsResource(
        UUID id,
        String name,
        RoleDetailsResource defaultRole,
        Set<RoleDetailsResource> roles
){}
