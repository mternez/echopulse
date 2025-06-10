package fr.mternez.echopulse.core.server.port.in;

import fr.mternez.echopulse.core.common.domain.error.*;
import fr.mternez.echopulse.core.common.domain.model.Membership;
import fr.mternez.echopulse.core.server.application.command.CreateMembershipCmd;
import fr.mternez.echopulse.core.server.application.command.DeleteMembershipCmd;

public interface MembershipCommandService {

    /**
     * Adds a user to a server.
     * @param cmd
     */
    Membership execute(CreateMembershipCmd cmd) throws UserNotFound, ServerNotFound, RoleNotFound, PersistenceException;

    /**
     * Removes a user from a server.
     * @param cmd
     */
    void execute(DeleteMembershipCmd cmd) throws MembershipNotFound, PersistenceException;
}
