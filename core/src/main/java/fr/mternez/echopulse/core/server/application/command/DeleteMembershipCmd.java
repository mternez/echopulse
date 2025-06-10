package fr.mternez.echopulse.core.server.application.command;

import fr.mternez.echopulse.core.common.application.InvocationSource;
import fr.mternez.echopulse.core.common.domain.model.ServerId;
import fr.mternez.echopulse.core.common.domain.model.UserId;

public record DeleteMembershipCmd(InvocationSource invocationSource, ServerId serverId, UserId userId) implements ServerCommand {
}
