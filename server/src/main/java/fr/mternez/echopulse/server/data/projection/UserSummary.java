package fr.mternez.echopulse.server.data.projection;

import java.util.Set;
import java.util.UUID;

public interface UserSummary {

    UUID getId();
    String getUsername();
    String getDisplayName();
    Set<MembershipSummary> getMemberships();
}
