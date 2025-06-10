package fr.mternez.echopulse.core.server.application.command;

import fr.mternez.echopulse.core.common.application.InvocationSource;
import fr.mternez.echopulse.core.common.domain.model.ServerId;

public record DeleteServerCmd(InvocationSource invocationSource, ServerId serverId) implements ServerCommand {}
