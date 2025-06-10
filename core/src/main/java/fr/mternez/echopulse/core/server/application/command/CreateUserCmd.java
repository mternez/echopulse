package fr.mternez.echopulse.core.server.application.command;

import fr.mternez.echopulse.core.common.application.InvocationSource;

public record CreateUserCmd(InvocationSource invocationSource, String username) {}
