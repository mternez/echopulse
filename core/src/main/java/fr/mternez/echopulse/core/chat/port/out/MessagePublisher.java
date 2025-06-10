package fr.mternez.echopulse.core.chat.port.out;

import fr.mternez.echopulse.core.chat.domain.*;

public interface MessagePublisher {

    void publish(PostMessage postMessage);
    void publish(ChannelCreatedMessage message);
    void publish(ChannelDeletedMessage message);
    void publish(UserJoinedMessage message);
    void publish(UserLeftMessage message);
}
