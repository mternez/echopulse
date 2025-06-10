package fr.mternez.echopulse.core.common.event;

import fr.mternez.echopulse.core.common.domain.model.ServerId;
import fr.mternez.echopulse.core.common.domain.model.UserId;

import java.util.Set;

public record UserJoinedServer(UserId userId, ServerId serverId, String username, String displayName, Set<UserRole> roles) { }
