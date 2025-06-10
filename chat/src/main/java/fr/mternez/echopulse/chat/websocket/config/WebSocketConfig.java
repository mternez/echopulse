package fr.mternez.echopulse.chat.websocket.config;

import fr.mternez.echopulse.chat.websocket.config.handler.SetPrincipalHandshakeHandler;
import fr.mternez.echopulse.chat.websocket.config.interceptor.JwtChannelInterceptor;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.handler.invocation.HandlerMethodArgumentResolver;
import org.springframework.messaging.simp.config.ChannelRegistration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.security.messaging.context.AuthenticationPrincipalArgumentResolver;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

import java.util.List;

@Configuration
@EnableWebSocket
@EnableWebSocketMessageBroker
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {

    private final JwtChannelInterceptor jwtChannelInterceptor;
    private final SetPrincipalHandshakeHandler setPrincipalHandshakeHandler;

    WebSocketConfig(
            final JwtChannelInterceptor jwtChannelInterceptor,
            SetPrincipalHandshakeHandler setPrincipalHandshakeHandler
    ) {
        this.jwtChannelInterceptor = jwtChannelInterceptor;
        this.setPrincipalHandshakeHandler = setPrincipalHandshakeHandler;
    }

    @Override
    public void configureMessageBroker(MessageBrokerRegistry config) {
        // Inbound messages endpoints prefix
        config.setApplicationDestinationPrefixes("/app");
        // Subscription endpoints
        config.enableSimpleBroker("/channels", "/servers");
    }

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {

        registry.addEndpoint("/ws")
                .setAllowedOriginPatterns("http://localhost:4200")
                .setHandshakeHandler(this.setPrincipalHandshakeHandler);

    }

    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> argumentResolvers) {
        argumentResolvers.add(new AuthenticationPrincipalArgumentResolver());
    }

    @Override
    public void configureClientInboundChannel(ChannelRegistration registration) {
        registration.interceptors(jwtChannelInterceptor);
    }
}
