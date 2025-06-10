package fr.mternez.echopulse.core.common.domain.model;

import java.util.UUID;

public class UserId extends DomainModelUuid {
    public UserId() { super(); }
    public UserId(final UUID value) { super(value); }
}
