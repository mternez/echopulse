package fr.mternez.echopulse.core.chat.application;

import fr.mternez.echopulse.core.chat.domain.PostMessage;
import fr.mternez.echopulse.core.chat.port.in.PostEventHandler;
import fr.mternez.echopulse.core.chat.port.out.MessagePublisher;
import fr.mternez.echopulse.core.common.event.PostSent;
import fr.mternez.echopulse.core.chat.port.out.PostEventPublisher;

public class PostEventHandlerImpl implements PostEventHandler {

    private final MessagePublisher messagePublisher;
    private final PostEventPublisher eventPublisher;

    public PostEventHandlerImpl(
            final MessagePublisher messagePublisher,
            final PostEventPublisher eventPublisher
    ) {
        this.messagePublisher = messagePublisher;
        this.eventPublisher = eventPublisher;
    }

    @Override
    public void handleMessage(final PostMessage message) {
        final PostSent event = new PostSent(message);
        this.eventPublisher.publish(event);
    }

    @Override
    public void handleEvent(final PostSent event) {
        final PostMessage postMessage = event.postMessage();
        this.messagePublisher.publish(postMessage);
    }
}
