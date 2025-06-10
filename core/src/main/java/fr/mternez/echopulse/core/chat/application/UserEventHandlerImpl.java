package fr.mternez.echopulse.core.chat.application;

import fr.mternez.echopulse.core.chat.domain.UserJoinedMessage;
import fr.mternez.echopulse.core.chat.domain.UserLeftMessage;
import fr.mternez.echopulse.core.chat.port.in.UserEventHandler;
import fr.mternez.echopulse.core.chat.port.out.MessagePublisher;
import fr.mternez.echopulse.core.common.event.UserJoinedServer;
import fr.mternez.echopulse.core.common.event.UserLeftServer;

public class UserEventHandlerImpl implements UserEventHandler {

    private final MessagePublisher messagePublisher;

    public UserEventHandlerImpl(
            final MessagePublisher messagePublisher
    ) {
        this.messagePublisher = messagePublisher;
    }

    @Override
    public void handleEvent(UserJoinedServer event) {
        this.messagePublisher.publish(new UserJoinedMessage(event));
    }

    @Override
    public void handleEvent(UserLeftServer event) {
        this.messagePublisher.publish(new UserLeftMessage(event));
    }
}
