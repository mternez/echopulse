package fr.mternez.echopulse.core.chat.port.in;

import fr.mternez.echopulse.core.chat.domain.PostMessage;
import fr.mternez.echopulse.core.common.event.PostSent;

public interface PostEventHandler {
    void handleMessage(PostMessage message);
    void handleEvent(PostSent event);
}
