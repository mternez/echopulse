package fr.mternez.echopulse.server.kafka.adapter;

import fr.mternez.echopulse.core.common.event.ChannelCreated;
import fr.mternez.echopulse.core.common.event.ChannelDeleted;
import fr.mternez.echopulse.core.common.event.UserJoinedServer;
import fr.mternez.echopulse.core.common.event.UserLeftServer;
import fr.mternez.echopulse.core.server.port.out.EventPublisher;
import fr.mternez.echopulse.server.kafka.config.KafkaTopic;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
public class EventPublisherAdapter implements EventPublisher {

    private final KafkaTemplate<String, ChannelCreated> channelCreatedTemplate;
    private final KafkaTemplate<String, ChannelDeleted> channelDeletedTemplate;
    private final KafkaTemplate<String, UserJoinedServer> userJoinedServerTemplate;
    private final KafkaTemplate<String, UserLeftServer> userLeftServerTemplate;

    EventPublisherAdapter(
            final KafkaTemplate<String, ChannelCreated> channelCreatedTemplate,
            final KafkaTemplate<String, ChannelDeleted> channelDeletedTemplate,
            final KafkaTemplate<String, UserJoinedServer> userJoinedServerTemplate,
            final KafkaTemplate<String, UserLeftServer> userLeftServerTemplate
    ) {
        this.channelCreatedTemplate = channelCreatedTemplate;
        this.channelDeletedTemplate = channelDeletedTemplate;
        this.userJoinedServerTemplate = userJoinedServerTemplate;
        this.userLeftServerTemplate = userLeftServerTemplate;
    }

    @Override
    public void publish(ChannelCreated event) {
        this.channelCreatedTemplate.send(KafkaTopic.CREATE_CHANNEL.getTopic(), event);
    }

    @Override
    public void publish(ChannelDeleted event) {
        this.channelDeletedTemplate.send(KafkaTopic.DELETE_CHANNEL.getTopic(), event);
    }

    @Override
    public void publish(UserJoinedServer event) {
        this.userJoinedServerTemplate.send(KafkaTopic.USER_JOIN_SERVER.getTopic(), event);
    }

    @Override
    public void publish(UserLeftServer event) {
        this.userLeftServerTemplate.send(KafkaTopic.USER_LEAVE_SERVER.getTopic(), event);
    }
}
