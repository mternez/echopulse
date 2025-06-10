package fr.mternez.echopulse.server.data.projection;

import java.util.Set;
import java.util.UUID;

public interface ServerDetails {
    UUID getId();
    String getName();
    RoleDetails getDefaultRole();
    Set<RoleDetails> getRoles();
}
