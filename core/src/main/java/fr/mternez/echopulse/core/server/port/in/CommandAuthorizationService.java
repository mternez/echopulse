package fr.mternez.echopulse.core.server.port. in;

import fr.mternez.echopulse.core.common.domain.error.PermissionDenied;
import fr.mternez.echopulse.core.server.application.command.*;

public interface CommandAuthorizationService {
    void authorize(CreateUserCmd cmd) throws PermissionDenied;
    void authorize(CreateServerCmd cmd) throws PermissionDenied;
    void authorize(DeleteServerCmd cmd) throws PermissionDenied;
    void authorize(CreateChannelCmd cmd) throws PermissionDenied;
    void authorize(DeleteChannelCmd cmd) throws PermissionDenied;
    void authorize(CreateMembershipCmd cmd) throws PermissionDenied;
    void authorize(DeleteMembershipCmd cmd) throws PermissionDenied;
    void authorize(CreateRoleCmd cmd) throws PermissionDenied;
    void authorize(DeleteRoleCmd cmd) throws PermissionDenied;
    void authorize(AssignRoleCmd cmd) throws PermissionDenied;
    void authorize(UnassignRoleCmd cmd) throws PermissionDenied;
}