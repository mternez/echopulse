package fr.mternez.echopulse.core.common.domain.model;

import com.github.f4b6a3.uuid.UuidCreator;

import java.util.UUID;

public abstract class DomainModelUuid {

    private final UUID value;

    public DomainModelUuid() {
        this.value = UuidCreator.getTimeOrderedEpoch();
    }
    public DomainModelUuid(final UUID value) {
        this.value = value;
    }

    public UUID getValue() {
        return value;
    }

    @Override public int hashCode() { return value.hashCode(); }

    @Override public boolean equals(Object o) {
        return (o instanceof DomainModelUuid dm) && value.equals(dm.value);
    }
}
