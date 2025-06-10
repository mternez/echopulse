package fr.mternez.echopulse.core.server.port.out;

import fr.mternez.echopulse.core.common.domain.model.Membership;
import fr.mternez.echopulse.core.common.domain.model.ServerId;
import fr.mternez.echopulse.core.common.domain.model.UserId;
import fr.mternez.echopulse.core.common.domain.error.PersistenceException;

import java.util.Set;

public interface MembershipRepository {

    Membership persistNew(Membership membership) throws PersistenceException;

    void deleteByServerIdUserId(ServerId serverId, UserId userId) throws PersistenceException;

    void deleteAllByServerId(ServerId serverId);
}
