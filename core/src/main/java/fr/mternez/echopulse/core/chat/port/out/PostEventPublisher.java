package fr.mternez.echopulse.core.chat.port.out;

import fr.mternez.echopulse.core.common.event.PostSent;

public interface PostEventPublisher {
    void publish(PostSent event);
}
