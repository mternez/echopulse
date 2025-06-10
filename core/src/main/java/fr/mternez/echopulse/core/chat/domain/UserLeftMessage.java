package fr.mternez.echopulse.core.chat.domain;

import fr.mternez.echopulse.core.common.event.UserLeftServer;

public record UserLeftMessage(UserLeftServer event) { }
