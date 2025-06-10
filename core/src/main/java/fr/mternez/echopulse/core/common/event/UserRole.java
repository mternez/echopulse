package fr.mternez.echopulse.core.common.event;

import fr.mternez.echopulse.core.common.domain.model.Permission;

import java.util.Set;

public record UserRole(String name, String description, String color, Set<Permission> permissions) {
}
