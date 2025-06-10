package fr.mternez.echopulse.core.server.application.command;

import fr.mternez.echopulse.core.common.application.InvocationSource;
import fr.mternez.echopulse.core.common.domain.model.ChannelId;
import fr.mternez.echopulse.core.common.domain.model.ServerId;

public record DeleteChannelCmd(InvocationSource invocationSource, ServerId serverId, ChannelId channelId) implements ServerCommand {
}
