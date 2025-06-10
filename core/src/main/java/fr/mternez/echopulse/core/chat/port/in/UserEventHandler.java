package fr.mternez.echopulse.core.chat.port.in;

import fr.mternez.echopulse.core.common.event.UserJoinedServer;
import fr.mternez.echopulse.core.common.event.UserLeftServer;

public interface UserEventHandler {
    void handleEvent(UserJoinedServer event);
    void handleEvent(UserLeftServer event);
}
