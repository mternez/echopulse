package fr.mternez.echopulse.core.common.application;

import fr.mternez.echopulse.core.common.domain.model.User;

import java.time.Instant;

public record InvocationSource(User user, Instant timestamp) {
}
