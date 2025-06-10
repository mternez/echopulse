package fr.mternez.echopulse.server.data.model;

import jakarta.persistence.*;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Entity
@Table(name = "users")
public class UserEntity implements Serializable {

    @Id
    private UUID id;

    @Column(name = "username")
    private String username;

    @Column(name = "display_name")
    private String displayName;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "user", orphanRemoval = true)
    private Set<MembershipEntity> memberships = new HashSet<>();

    public void setId(UUID id) {
        this.id = id;
    }

    public UUID getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public Set<MembershipEntity> getMemberships() {
        return memberships;
    }

    public void setMemberships(final Set<MembershipEntity> memberships) {
        this.memberships = memberships;
    }
}
