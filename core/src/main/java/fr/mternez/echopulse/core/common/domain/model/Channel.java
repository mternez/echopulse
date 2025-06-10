package fr.mternez.echopulse.core.common.domain.model;

public class Channel {

    private final ChannelId id;
    private final ServerId serverId;
    private final String name;

    public Channel(final ChannelId id, final ServerId serverId, final String name) {
        if (serverId == null) {
            throw new IllegalArgumentException("Server id cannot be null");
        }
        if(name == null || name.isEmpty()) {
            throw new IllegalArgumentException("Channel name cannot be null or empty");
        }
        this.id = id;
        this.serverId = serverId;
        this.name = name;
    }

    public Channel(final ServerId serverId, final String name) {
        if (serverId == null) {
            throw new IllegalArgumentException("Server id cannot be null");
        }
        if(name == null || name.isEmpty()) {
            throw new IllegalArgumentException("Channel name cannot be null or empty");
        }
        this.id = new ChannelId();
        this.serverId = serverId;
        this.name = name;
    }

    public ChannelId getId() {
        return id;
    }

    public ServerId getServerId() {
        return serverId;
    }

    public String getName() {
        return name;
    }
}
