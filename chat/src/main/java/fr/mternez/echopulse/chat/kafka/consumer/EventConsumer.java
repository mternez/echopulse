package fr.mternez.echopulse.chat.kafka.consumer;

import fr.mternez.echopulse.chat.data.model.Post;
import fr.mternez.echopulse.chat.data.repository.PostRepository;
import fr.mternez.echopulse.core.chat.port.in.ChannelEventHandler;
import fr.mternez.echopulse.core.chat.port.in.PostEventHandler;
import fr.mternez.echopulse.core.chat.port.in.UserEventHandler;
import fr.mternez.echopulse.core.common.event.*;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
public class EventConsumer {

    private final PostEventHandler postEventHandler;
    private final ChannelEventHandler channelEventHandler;
    private final UserEventHandler userEventHandler;
    private final PostRepository postRepository;

    EventConsumer(
            final PostEventHandler postEventHandler,
            final ChannelEventHandler channelEventHandler,
            final UserEventHandler userEventHandler,
            final PostRepository postRepository
    ) {
        this.postEventHandler = postEventHandler;
        this.channelEventHandler = channelEventHandler;
        this.userEventHandler = userEventHandler;
        this.postRepository = postRepository;
    }

    @KafkaListener(topics = "channels.send-message", groupId = "chat-consumer-group")
    public void processMessage(final PostSent event) {
        final Post post = new Post();
        post.setUsername(event.postMessage().username());
        post.setChannelId(event.postMessage().channelId().getValue());
        post.setContent(event.postMessage().content());
        post.setTimestamp(event.postMessage().timestamp());
        this.postRepository.save(post);
        this.postEventHandler.handleEvent(event);
    }

    @KafkaListener(topics = "channels.create", groupId = "chat-consumer-group")
    public void processMessage(final ChannelCreated event) {
        this.channelEventHandler.handleEvent(event);
    }

    @KafkaListener(topics = "channels.delete", groupId = "chat-consumer-group")
    public void processMessage(final ChannelDeleted event) {
        this.postRepository.deleteAllByChannelId(event.id().getValue());
        this.channelEventHandler.handleEvent(event);
    }

    @KafkaListener(topics = "user.join-server", groupId = "chat-consumer-group")
    public void processMessage(final UserJoinedServer event) {
        this.userEventHandler.handleEvent(event);
    }

    @KafkaListener(topics = "user.leave-server", groupId = "chat-consumer-group")
    public void processMessage(final UserLeftServer event) {
        this.userEventHandler.handleEvent(event);
    }
}
