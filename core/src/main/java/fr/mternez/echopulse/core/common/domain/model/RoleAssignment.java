package fr.mternez.echopulse.core.common.domain.model;

public class RoleAssignment {

    private final Membership membership;
    private final Role role;

    public RoleAssignment (final Membership membership, final Role role) {
        this.membership = membership;
        this.role = role;
    }
}
