package fr.mternez.echopulse.core.common.domain.model;

public enum Permission {

    DELETE_SERVER("Delete server."),
    MANAGE_CHANNELS("Manage channels."),
    MANAGE_MEMBERS("Manage users."),
    MANAGE_ROLES("Manage roles.");

    private final String description;

    Permission(final String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}
