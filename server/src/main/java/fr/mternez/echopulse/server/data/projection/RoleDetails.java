package fr.mternez.echopulse.server.data.projection;

import fr.mternez.echopulse.core.common.domain.model.Permission;

import java.util.Set;

public interface RoleDetails {
    String getName();
    String getDescription();
    String getColor();
    Set<Permission> getPermissions();
}
