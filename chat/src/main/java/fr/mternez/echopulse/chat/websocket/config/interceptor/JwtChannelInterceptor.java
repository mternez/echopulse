package fr.mternez.echopulse.chat.websocket.config.interceptor;

import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtException;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class JwtChannelInterceptor implements ChannelInterceptor {

    private final JwtDecoder jwtDecoder;

    JwtChannelInterceptor(final JwtDecoder jwtDecoder) {
        this.jwtDecoder = jwtDecoder;
    }

    @Override
    public Message<?> preSend(final Message<?> message, final MessageChannel channel) {
        StompHeaderAccessor accessor = StompHeaderAccessor.wrap(message);
        if (StompCommand.SEND.equals(accessor.getCommand()) || StompCommand.SUBSCRIBE.equals(accessor.getCommand())) {
            String token = getTokenFromHeader(accessor);
            if (token != null) {
                try {
                    final Jwt jwt = jwtDecoder.decode(token);
                    final Authentication auth = new JwtAuthenticationToken(jwt);
                    accessor.setUser(auth);
                    accessor.setHeader(SimpMessageHeaderAccessor.USER_HEADER, auth);
                    SecurityContextHolder.getContext().setAuthentication(auth);
                } catch (JwtException e) {
                    throw new IllegalArgumentException("Invalid JWT token");
                }
            } else {
                throw new IllegalArgumentException("Missing Authorization header");
            }
        }
        return message;
    }

    private String getTokenFromHeader(final StompHeaderAccessor accessor) {
        List<String> authHeaders = accessor.getNativeHeader("Authorization");
        if (authHeaders != null && !authHeaders.isEmpty()) {
            String bearer = authHeaders.get(0);
            if (bearer.startsWith("Bearer ")) {
                return bearer.substring(7);
            }
        }
        return null;
    }
}
