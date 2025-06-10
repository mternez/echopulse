package fr.mternez.echopulse.core.common.event;

import fr.mternez.echopulse.core.common.domain.model.ServerId;
import fr.mternez.echopulse.core.common.domain.model.UserId;

public record UserLeftServer(UserId userId, ServerId serverId) { }
