package fr.mternez.echopulse.chat.kafka.adapter;

import fr.mternez.echopulse.chat.kafka.config.KafkaTopic;
import fr.mternez.echopulse.core.common.event.PostSent;
import fr.mternez.echopulse.core.chat.port.out.PostEventPublisher;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
public class PostEventPublisherAdapter implements PostEventPublisher {

    private final KafkaTemplate<String, PostSent> postSentTemplate;

    PostEventPublisherAdapter(
            final KafkaTemplate<String, PostSent> postSentTemplate
    ) {
        this.postSentTemplate = postSentTemplate;
    }

    @Override
    public void publish(final PostSent event) {
        this.postSentTemplate.send(KafkaTopic.SEND_MESSAGE_ON_CHANNEL.getTopic(), event);
    }
}
