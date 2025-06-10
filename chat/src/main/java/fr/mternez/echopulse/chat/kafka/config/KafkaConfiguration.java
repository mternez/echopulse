package fr.mternez.echopulse.chat.kafka.config;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.config.TopicBuilder;
import org.springframework.kafka.support.converter.ByteArrayJsonMessageConverter;
import org.springframework.kafka.support.converter.JsonMessageConverter;

@Configuration
@EnableKafka
public class KafkaConfiguration {

    @Bean
    public NewTopic sendMessageOnChannelTopic() {
        return TopicBuilder.name(KafkaTopic.SEND_MESSAGE_ON_CHANNEL.getTopic())
                .build();
    }

    @Bean
    public NewTopic createChannelTopic() {
        return TopicBuilder.name(KafkaTopic.CREATE_CHANNEL.getTopic())
                .build();
    }

    @Bean
    public NewTopic deleteChannelTopic() {
        return TopicBuilder.name(KafkaTopic.DELETE_CHANNEL.getTopic())
                .build();
    }

    @Bean
    public NewTopic userJoinServerTopic() {
        return TopicBuilder.name(KafkaTopic.USER_JOIN_SERVER.getTopic())
                .build();
    }

    @Bean
    public NewTopic userLeaveServerTopic() {
        return TopicBuilder.name(KafkaTopic.USER_LEAVE_SERVER.getTopic())
                .build();
    }

    @Bean
    public JsonMessageConverter jsonMessageConverter() {
        return new ByteArrayJsonMessageConverter();
    }
}
