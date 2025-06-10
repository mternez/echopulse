package fr.mternez.echopulse.server.data.model;

import java.io.Serializable;

public class MembershipPK implements Serializable {

    private UserEntity user;

    public MembershipPK(final UserEntity user) {
        this.user = user;
    }

    public MembershipPK() {

    }

    public UserEntity getUser() {
        return user;
    }

    public void setUser(UserEntity user) {
        this.user = user;
    }
}
