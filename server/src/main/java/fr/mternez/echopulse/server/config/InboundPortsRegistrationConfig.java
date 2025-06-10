package fr.mternez.echopulse.server.config;

import fr.mternez.echopulse.core.server.application.service.*;
import fr.mternez.echopulse.core.server.port.in.*;
import fr.mternez.echopulse.server.data.adapter.ChannelRepositoryJpaAdapter;
import fr.mternez.echopulse.server.data.adapter.MembershipRepositoryJpaAdapter;
import fr.mternez.echopulse.server.data.adapter.ServerRepositoryJpaAdapter;
import fr.mternez.echopulse.server.data.adapter.UserRepositoryJpaAdapter;
import fr.mternez.echopulse.server.kafka.adapter.EventPublisherAdapter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class InboundPortsRegistrationConfig {

    private final UserRepositoryJpaAdapter userRepositoryJpaAdapter;
    private final ServerRepositoryJpaAdapter serverRepositoryJpaAdapter;
    private final MembershipRepositoryJpaAdapter membershipRepositoryJpaAdapter;
    private final ChannelRepositoryJpaAdapter channelRepositoryJpaAdapter;
    private final EventPublisherAdapter eventPublisherAdapter;

    InboundPortsRegistrationConfig(
            final UserRepositoryJpaAdapter userRepositoryJpaAdapter,
            final ServerRepositoryJpaAdapter serverRepositoryJpaAdapter,
            final MembershipRepositoryJpaAdapter membershipRepositoryJpaAdapter,
            final ChannelRepositoryJpaAdapter channelRepositoryJpaAdapter,
            final EventPublisherAdapter eventPublisherAdapter
    ) {
        this.userRepositoryJpaAdapter = userRepositoryJpaAdapter;
        this.serverRepositoryJpaAdapter = serverRepositoryJpaAdapter;
        this.membershipRepositoryJpaAdapter = membershipRepositoryJpaAdapter;
        this.channelRepositoryJpaAdapter = channelRepositoryJpaAdapter;
        this.eventPublisherAdapter = eventPublisherAdapter;
    }

    @Bean
    UserCommandService userCommandService() {
        return new UserCommandServiceImpl(this.userRepositoryJpaAdapter);
    }

    @Bean
    ServerCommandService serverCommandService() {
        return new ServerCommandServiceImpl(this.userRepositoryJpaAdapter, this.serverRepositoryJpaAdapter, this.membershipRepositoryJpaAdapter, this.channelRepositoryJpaAdapter);
    }

    @Bean
    MembershipCommandService membershipCommandService() {
        return new MembershipCommandServiceImpl(this.userRepositoryJpaAdapter, this.membershipRepositoryJpaAdapter, this.serverRepositoryJpaAdapter, this.eventPublisherAdapter);
    }

    @Bean
    ChannelCommandService channelCommandService() {
        return new ChannelCommandServiceImpl(this.channelRepositoryJpaAdapter, this.serverRepositoryJpaAdapter, this.eventPublisherAdapter);
    }

    @Bean
    CommandAuthorizationService commandAuthorizationService() {
        return new CommandAuthorizationServiceImpl();
    }
}
