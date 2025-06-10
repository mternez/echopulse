package fr.mternez.echopulse.core.common.domain.model;

import java.util.UUID;

public class ChannelId extends DomainModelUuid {
    public ChannelId() { super(); }
    public ChannelId(final UUID value) { super(value); }
}
