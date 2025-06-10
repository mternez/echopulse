package fr.mternez.echopulse.server.data.projection;

import java.util.Set;

public interface MembershipSummary {
    ServerSummary getServer();
    Set<RoleDetails> getRoles();
}
