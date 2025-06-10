package fr.mternez.echopulse.server.data.model;

import jakarta.persistence.*;

import java.util.UUID;

@Entity
@Table(name = "channels")
public class ChannelEntity {

    @Id
    private UUID id;

    private String name;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "server_id")
    private ServerEntity server;

    public void setId(UUID id) {
        this.id = id;
    }

    public UUID getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ServerEntity getServer() {
        return server;
    }

    public void setServer(ServerEntity server) {
        this.server = server;
    }
}
