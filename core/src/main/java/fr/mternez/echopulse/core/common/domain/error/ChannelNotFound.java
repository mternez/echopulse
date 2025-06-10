package fr.mternez.echopulse.core.common.domain.error;

import fr.mternez.echopulse.core.common.domain.model.ChannelId;

public class ChannelNotFound extends DomainError {

    private final ChannelId channelId;

    public ChannelNotFound(final ChannelId channelId) {
        this.channelId = channelId;
    }

    public ChannelNotFound(final ChannelId channelId, Exception e) {
        super(e);
        this.channelId = channelId;
    }

    public ChannelId getChannelId() {
        return this.channelId;
    }
}
