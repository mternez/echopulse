package fr.mternez.echopulse.core.chat.port.in;

import fr.mternez.echopulse.core.common.event.ChannelCreated;
import fr.mternez.echopulse.core.common.event.ChannelDeleted;

public interface ChannelEventHandler {
    void handleEvent(ChannelCreated event);
    void handleEvent(ChannelDeleted event);
}
