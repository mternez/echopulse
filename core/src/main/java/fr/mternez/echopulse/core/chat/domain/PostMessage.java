package fr.mternez.echopulse.core.chat.domain;

import fr.mternez.echopulse.core.common.domain.model.ChannelId;

import java.time.Instant;

public record PostMessage(String username, ChannelId channelId, String content, Instant timestamp) {
}
