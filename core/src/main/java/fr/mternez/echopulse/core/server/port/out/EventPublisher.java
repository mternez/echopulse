package fr.mternez.echopulse.core.server.port.out;

import fr.mternez.echopulse.core.common.event.ChannelCreated;
import fr.mternez.echopulse.core.common.event.ChannelDeleted;
import fr.mternez.echopulse.core.common.event.UserJoinedServer;
import fr.mternez.echopulse.core.common.event.UserLeftServer;

public interface EventPublisher {
    void publish(ChannelCreated event);
    void publish(ChannelDeleted event);
    void publish(UserJoinedServer event);
    void publish(UserLeftServer event);
}
