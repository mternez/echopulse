package fr.mternez.echopulse.core.common.domain.error;

import fr.mternez.echopulse.core.common.domain.model.ServerId;

public class ServerNotFound extends DomainError {

    private final ServerId serverId;

    public ServerNotFound(final ServerId serverId) {
        this.serverId = serverId;
    }

    public ServerNotFound(final ServerId serverId, final Exception e) {
        super(e);
        this.serverId = serverId;
    }

    public ServerId getServerId() {
        return serverId;
    }
}
