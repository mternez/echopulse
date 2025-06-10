package fr.mternez.echopulse.chat.websocket.controller;

import fr.mternez.echopulse.core.chat.domain.PostMessage;
import fr.mternez.echopulse.core.chat.port.in.PostEventHandler;
import fr.mternez.echopulse.core.common.domain.model.ChannelId;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.stereotype.Controller;

import java.security.Principal;
import java.time.Instant;
import java.util.UUID;

@Controller
@MessageMapping("/channels")
public class ChannelController {

    private final PostEventHandler postEventHandler;

    ChannelController(
            final PostEventHandler postEventHandler
    ) {
        this.postEventHandler = postEventHandler;
    }

    @MessageMapping("{channelId}")
    public void handleMessage(@DestinationVariable final String channelId, @Payload final String content, final Principal auth) {
        final String username = ((JwtAuthenticationToken) auth).getTokenAttributes().get("preferred_username").toString();
        final UUID channelUUID = UUID.fromString(channelId);
        final PostMessage message = new PostMessage(username, new ChannelId(channelUUID), content, Instant.now());
        this.postEventHandler.handleMessage(message);
    }
}
