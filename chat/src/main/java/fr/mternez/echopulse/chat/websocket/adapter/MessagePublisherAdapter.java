package fr.mternez.echopulse.chat.websocket.adapter;

import fr.mternez.echopulse.chat.websocket.config.WebSocketTopic;
import fr.mternez.echopulse.core.chat.domain.*;
import fr.mternez.echopulse.core.chat.port.out.MessagePublisher;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;

import java.util.UUID;

import static java.lang.String.format;

@Component
public class MessagePublisherAdapter implements MessagePublisher {

    private final SimpMessagingTemplate messagingTemplate;

    MessagePublisherAdapter(
            final SimpMessagingTemplate messagingTemplate
    ) {
        this.messagingTemplate = messagingTemplate;
    }

    @Override
    public void publish(final PostMessage postMessage) {
        final UUID channelId = postMessage.channelId().getValue();
        this.messagingTemplate.convertAndSend(format(WebSocketTopic.SEND_MESSAGE_ON_CHANNEL.getTopic(), channelId.toString()), postMessage);
    }

    @Override
    public void publish(ChannelCreatedMessage message) {
        final UUID serverId = message.event().serverId().getValue();
        this.messagingTemplate.convertAndSend(format(WebSocketTopic.CHANNEL_CREATED.getTopic(), serverId.toString()), message);
    }

    @Override
    public void publish(ChannelDeletedMessage message) {
        final UUID serverId = message.event().serverId().getValue();
        this.messagingTemplate.convertAndSend(format(WebSocketTopic.CHANNEL_DELETED.getTopic(), serverId.toString()), message);
    }

    @Override
    public void publish(UserJoinedMessage message) {
        final UUID serverId = message.event().serverId().getValue();
        this.messagingTemplate.convertAndSend(format(WebSocketTopic.USER_JOINED_SERVER.getTopic(), serverId.toString()), message);
    }

    @Override
    public void publish(UserLeftMessage message) {
        final UUID serverId = message.event().serverId().getValue();
        this.messagingTemplate.convertAndSend(format(WebSocketTopic.USER_LEFT_SERVER.getTopic(), serverId.toString()), message);
    }
}
