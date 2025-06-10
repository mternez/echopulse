package fr.mternez.echopulse.server.resource.model;

import fr.mternez.echopulse.server.data.projection.MembershipSummary;

import java.util.Set;
import java.util.UUID;

public record UserSummaryResource(UUID id, String username, String displayName, Set<MembershipSummary> memberships) {}
