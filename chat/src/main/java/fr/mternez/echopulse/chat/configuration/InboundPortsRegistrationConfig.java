package fr.mternez.echopulse.chat.configuration;

import fr.mternez.echopulse.chat.kafka.adapter.PostEventPublisherAdapter;
import fr.mternez.echopulse.chat.websocket.adapter.MessagePublisherAdapter;
import fr.mternez.echopulse.core.chat.application.ChannelEventHandlerImpl;
import fr.mternez.echopulse.core.chat.application.PostEventHandlerImpl;
import fr.mternez.echopulse.core.chat.application.UserEventHandlerImpl;
import fr.mternez.echopulse.core.chat.port.in.ChannelEventHandler;
import fr.mternez.echopulse.core.chat.port.in.PostEventHandler;
import fr.mternez.echopulse.core.chat.port.in.UserEventHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class InboundPortsRegistrationConfig {

    private final PostEventPublisherAdapter postEventPublisherAdapter;
    private final MessagePublisherAdapter postMessagePublisherAdapter;
    private final MessagePublisherAdapter messagePublisherAdapter;

    InboundPortsRegistrationConfig(
            final PostEventPublisherAdapter postEventPublisherAdapter,
            final MessagePublisherAdapter postMessagePublisherAdapter,
            final MessagePublisherAdapter messagePublisherAdapter
    ) {
        this.postEventPublisherAdapter = postEventPublisherAdapter;
        this.postMessagePublisherAdapter = postMessagePublisherAdapter;
        this.messagePublisherAdapter = messagePublisherAdapter;
    }

    @Bean
    public PostEventHandler getPostHandlerBean() {
        return new PostEventHandlerImpl(this.postMessagePublisherAdapter, this.postEventPublisherAdapter);
    }

    @Bean
    public ChannelEventHandler getChannelEventHandlerBean() {
        return new ChannelEventHandlerImpl(this.messagePublisherAdapter);
    }

    @Bean
    public UserEventHandler getUserEventHandlerBean() {
        return new UserEventHandlerImpl(this.messagePublisherAdapter);
    }
}
