package fr.mternez.echopulse.core.common.domain.model;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

public class Role {

    private String name;

    private String description;

    private String color;

    private final Set<Permission> permissions = new HashSet<>(1);

    public Role(String name, final Permission ...permissions) {
        this.setName(name);
        this.addPermission(permissions);
    }

    public Role(String name, final String description, final String color, final Permission ...permissions) {
        this.setName(name);
        this.setDescription(description);
        this.setColor(color);
        this.addPermission(permissions);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        if(name == null || name.isEmpty()) {
            throw new IllegalArgumentException("Role name cannot be null or empty");
        }
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(final String description) {
        this.description = description;
    }

    public String getColor() {
        return color;
    }

    public void setColor(final String color) {
        this.color = color;
    }

    public void addPermission(final Permission permission) {
        this.permissions.add(permission);
    }

    public void addPermission(final Permission ...permission) {
        if (permission != null) {
            this.permissions.addAll(Arrays.stream(permission).toList());
        }
    }

    public void removePermission(final Permission permission) {
        this.permissions.remove(permission);
    }

    public Set<Permission> getPermissions() {
        return Collections.unmodifiableSet(permissions);
    }
}
