package fr.mternez.echopulse.server.data.model;

import fr.mternez.echopulse.core.common.domain.model.Permission;
import fr.mternez.echopulse.server.data.converter.PermissionSetConverter;
import jakarta.persistence.*;

import java.util.Set;
import java.util.UUID;

@Entity
@Table(name = "roles")
public class RoleEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "server_id")
    private ServerEntity server;

    private String name;

    private String description;

    private String color;

    @Convert(converter = PermissionSetConverter.class)
    private Set<Permission> permissions;

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public ServerEntity getServer() {
        return server;
    }

    public void setServer(final ServerEntity server) {
        this.server = server;
    }

    public String getName() {
        return name;
    }

    public void setName(final String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(final String description) {
        this.description = description;
    }

    public String getColor() {
        return color;
    }

    public void setColor(final String color) {
        this.color = color;
    }

    public Set<Permission> getPermissions() {
        return permissions;
    }

    public void setPermissions(final Set<Permission> permissions) {
        this.permissions = permissions;
    }
}
