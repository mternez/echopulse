package fr.mternez.echopulse.core.server.application.command;

import fr.mternez.echopulse.core.common.application.InvocationSource;
import fr.mternez.echopulse.core.common.domain.model.ServerId;

public record CreateChannelCmd(InvocationSource invocationSource, ServerId serverId, String name) implements ServerCommand {
}
