package fr.mternez.echopulse.core.common.domain.error;

import fr.mternez.echopulse.core.common.domain.model.Permission;
import fr.mternez.echopulse.core.common.domain.model.ServerId;
import fr.mternez.echopulse.core.common.domain.model.UserId;

import static java.lang.String.format;

public class PermissionDenied extends DomainError {

    private final UserId userId;
    private final ServerId serverId;
    private final Permission permission;
    private final String reason;

    public PermissionDenied(final UserId userId, final ServerId serverId, final Permission permission) {
        this.userId = userId;
        this.serverId = serverId;
        this.permission = permission;
        this.reason = format("User ('%s') lacks permission '%s' on server ('%s').", userId.getValue(), serverId.getValue(), permission.getDescription());
    }

    public PermissionDenied(final UserId userId, final ServerId serverId, final String reason) {
        this.userId = userId;
        this.serverId = serverId;
        this.permission = null;
        this.reason = reason;
    }

    public UserId getUserId() {
        return userId;
    }

    public ServerId getServerId() {
        return serverId;
    }

    public Permission getPermission() {
        return permission;
    }

    public String getReason() {
        return reason;
    }
}
