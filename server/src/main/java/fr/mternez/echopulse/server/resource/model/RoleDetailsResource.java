package fr.mternez.echopulse.server.resource.model;

import fr.mternez.echopulse.core.common.domain.model.Permission;

import java.util.Set;

public record RoleDetailsResource(
        String name,
        String description,
        String color,
        Set<Permission> permissions
) {}
