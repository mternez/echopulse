package fr.mternez.echopulse.core.common.domain.error;

import fr.mternez.echopulse.core.common.domain.model.ServerId;

import java.util.HashSet;
import java.util.Set;

public class RoleNotFound extends DomainError {

    private final ServerId serverId;
    private final Set<String> names = new HashSet<>();

    public RoleNotFound(final ServerId serverId) {
        this.serverId = serverId;
    }

    public RoleNotFound(final ServerId serverId, final String name) {
        this.serverId = serverId;
        this.names.add(name);
    }

    public RoleNotFound(final ServerId serverId, final Set<String> names) {
        this.serverId = serverId;
        this.names.addAll(names);
    }

    public ServerId getServerId() {
        return this.serverId;
    }

    public Set<String> getNames() {
        return this.names;
    }
}
