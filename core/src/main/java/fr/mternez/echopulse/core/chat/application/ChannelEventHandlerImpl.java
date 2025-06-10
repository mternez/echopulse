package fr.mternez.echopulse.core.chat.application;

import fr.mternez.echopulse.core.chat.domain.ChannelCreatedMessage;
import fr.mternez.echopulse.core.chat.domain.ChannelDeletedMessage;
import fr.mternez.echopulse.core.chat.port.in.ChannelEventHandler;
import fr.mternez.echopulse.core.chat.port.out.MessagePublisher;
import fr.mternez.echopulse.core.common.event.ChannelCreated;
import fr.mternez.echopulse.core.common.event.ChannelDeleted;

public class ChannelEventHandlerImpl implements ChannelEventHandler {

    private final MessagePublisher messagePublisher;

    public ChannelEventHandlerImpl(
            final MessagePublisher messagePublisher
    ) {
        this.messagePublisher = messagePublisher;
    }


    @Override
    public void handleEvent(ChannelCreated event) {
        this.messagePublisher.publish(new ChannelCreatedMessage(event));
    }

    @Override
    public void handleEvent(ChannelDeleted event) {
        this.messagePublisher.publish(new ChannelDeletedMessage(event));
    }
}
