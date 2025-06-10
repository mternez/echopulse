package fr.mternez.echopulse.core.common.domain.error;

import fr.mternez.echopulse.core.common.domain.model.ServerId;
import fr.mternez.echopulse.core.common.domain.model.UserId;

public class RoleAssignmentNotFound extends DomainError {

    private final UserId userId;
    private final ServerId serverId;
    private final String name;

    public RoleAssignmentNotFound(final UserId userId, final ServerId serverId, final String name) {
        this.userId = userId;
        this.serverId = serverId;
        this.name = name;
    }

    public UserId getUserId() {
        return userId;
    }

    public ServerId getServerId() {
        return serverId;
    }

    public String getName() {
        return name;
    }
}
