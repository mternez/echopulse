package fr.mternez.echopulse.core.common.domain.error;

import fr.mternez.echopulse.core.common.domain.model.ServerId;
import fr.mternez.echopulse.core.common.domain.model.UserId;

public class MembershipNotFound extends DomainError {

    private final UserId userId;
    private final ServerId serverId;

    public MembershipNotFound(final UserId userId, final ServerId serverId) {
        this.userId = userId;
        this.serverId = serverId;
    }

    public UserId getUserId() {
        return userId;
    }

    public ServerId getServerId() {
        return serverId;
    }
}
