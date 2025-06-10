package fr.mternez.echopulse.core.chat.domain;

import fr.mternez.echopulse.core.common.event.UserJoinedServer;

public record UserJoinedMessage(UserJoinedServer event) {
}
