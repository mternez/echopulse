package fr.mternez.echopulse.server.data.projection;

import java.util.Set;
import java.util.UUID;

public interface ServerMemberDetails {

    ServerMemberUserSummary getUser();
    Set<RoleDetails> getRoles();

    interface ServerMemberUserSummary {
        UUID getId();
        String getUsername();
        String getDisplayName();
    }
}