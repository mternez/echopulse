package fr.mternez.echopulse.chat.websocket.config.handler;

import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServletServerHttpRequest;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtException;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.server.support.DefaultHandshakeHandler;

import java.security.Principal;
import java.util.Map;

@Component
public class SetPrincipalHandshakeHandler extends DefaultHandshakeHandler {

    private final JwtDecoder jwtDecoder;

    SetPrincipalHandshakeHandler(final JwtDecoder jwtDecoder) {
        this.jwtDecoder = jwtDecoder;
    }

    @Override
    protected boolean isValidOrigin(ServerHttpRequest request) {
        return true;
    }

    @Override
    protected Principal determineUser(ServerHttpRequest request, WebSocketHandler wsHandler, Map<String, Object> attributes) {
        if (request instanceof ServletServerHttpRequest servletRequest) {

            final String token = servletRequest.getServletRequest().getParameter("token");

            if (token == null) throw new IllegalArgumentException("Missing Authorization header");;

            try {
                final Jwt jwt = jwtDecoder.decode(token);
                final Authentication auth = new JwtAuthenticationToken(jwt);
                return auth;
            } catch (JwtException e) {
                throw new IllegalArgumentException("Invalid JWT token");
            }

        }
        return null;
    }
}
