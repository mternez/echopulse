package fr.mternez.echopulse.core.server.application.command;

import fr.mternez.echopulse.core.common.application.InvocationSource;
import fr.mternez.echopulse.core.common.domain.model.Permission;
import fr.mternez.echopulse.core.common.domain.model.ServerId;

import java.util.Set;

public record CreateRoleCmd(InvocationSource invocationSource, ServerId serverId, String name, Set<Permission> permissions) implements ServerCommand {
}
