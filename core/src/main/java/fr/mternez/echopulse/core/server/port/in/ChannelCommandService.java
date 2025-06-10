package fr.mternez.echopulse.core.server.port.in;

import fr.mternez.echopulse.core.server.application.command.CreateChannelCmd;
import fr.mternez.echopulse.core.server.application.command.DeleteChannelCmd;
import fr.mternez.echopulse.core.common.domain.error.ChannelNotFound;
import fr.mternez.echopulse.core.common.domain.error.PersistenceException;
import fr.mternez.echopulse.core.common.domain.error.ServerNotFound;
import fr.mternez.echopulse.core.common.domain.model.Channel;

public interface ChannelCommandService {

    Channel execute(CreateChannelCmd cmd) throws PersistenceException, ServerNotFound;

    void execute(DeleteChannelCmd cmd) throws ChannelNotFound, PersistenceException;
}
