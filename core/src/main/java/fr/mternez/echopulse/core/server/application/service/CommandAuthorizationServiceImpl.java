package fr.mternez.echopulse.core.server.application.service;

import fr.mternez.echopulse.core.common.domain.error.PermissionDenied;
import fr.mternez.echopulse.core.common.domain.model.Permission;
import fr.mternez.echopulse.core.common.domain.model.ServerId;
import fr.mternez.echopulse.core.common.domain.model.User;
import fr.mternez.echopulse.core.server.application.command.*;
import fr.mternez.echopulse.core.server.port.in.CommandAuthorizationService;

import static java.lang.String.format;

public class CommandAuthorizationServiceImpl implements CommandAuthorizationService {

    @Override
    public void authorize(final CreateUserCmd cmd) throws PermissionDenied {
        ;
    }

    @Override
    public void authorize(final CreateServerCmd cmd) throws PermissionDenied {
        ;
    }

    @Override
    public void authorize(final DeleteServerCmd cmd) throws PermissionDenied {
        this.assertPermission(cmd, Permission.DELETE_SERVER);
    }

    @Override
    public void authorize(final CreateChannelCmd cmd) throws PermissionDenied {
        this.assertPermission(cmd, Permission.MANAGE_CHANNELS);
    }

    @Override
    public void authorize(final DeleteChannelCmd cmd) throws PermissionDenied {
        this.assertPermission(cmd, Permission.MANAGE_CHANNELS);
    }

    @Override
    public void authorize(final CreateMembershipCmd cmd) throws PermissionDenied {
        this.assertPermission(cmd, Permission.MANAGE_MEMBERS);
    }

    @Override
    public void authorize(final DeleteMembershipCmd cmd) throws PermissionDenied {
        this.assertPermission(cmd, Permission.MANAGE_MEMBERS);
    }

    @Override
    public void authorize(final CreateRoleCmd cmd) throws PermissionDenied {
        this.assertPermission(cmd, Permission.MANAGE_ROLES);
    }

    @Override
    public void authorize(final DeleteRoleCmd cmd) throws PermissionDenied {
        this.assertPermission(cmd, Permission.MANAGE_ROLES);
    }

    @Override
    public void authorize(final AssignRoleCmd cmd) throws PermissionDenied {
        this.assertPermission(cmd, Permission.MANAGE_MEMBERS);
    }

    @Override
    public void authorize(final UnassignRoleCmd cmd) throws PermissionDenied {
        this.assertPermission(cmd, Permission.MANAGE_MEMBERS);
    }

    private boolean isMember(final User user, final ServerId serverId) {
        return user.isMemberOf(serverId);
    }

    private boolean hasPermission(final User user, final ServerId serverId, final Permission permission) {
        return user.getMembership(serverId).getRoles().stream().anyMatch(r -> r.getPermissions().contains(permission));
    }

    private void assertPermission(final ServerCommand cmd, final Permission permission) {
        final User user = cmd.invocationSource().user();
        final ServerId serverId = cmd.serverId();
        final boolean isMember = this.isMember(user, serverId);
        if(!isMember) {
            throw new PermissionDenied(user.getId(), serverId, format("User '%s' is not a member of server '%s'", user.getId().getValue(), serverId.getValue()));
        }
        if(!this.hasPermission(user, serverId, permission)) {
            throw new PermissionDenied(user.getId(), serverId, permission);
        }
    }
}
