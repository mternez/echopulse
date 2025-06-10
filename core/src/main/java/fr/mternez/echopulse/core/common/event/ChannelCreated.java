package fr.mternez.echopulse.core.common.event;

import fr.mternez.echopulse.core.common.domain.model.ChannelId;
import fr.mternez.echopulse.core.common.domain.model.ServerId;

public record ChannelCreated(ChannelId id, ServerId serverId, String name) { }
