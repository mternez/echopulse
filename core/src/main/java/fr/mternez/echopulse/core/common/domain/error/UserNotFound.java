package fr.mternez.echopulse.core.common.domain.error;

import fr.mternez.echopulse.core.common.domain.model.UserId;

public class UserNotFound extends DomainError {

    private final UserId userId;

    public UserNotFound(final UserId userId) {
        this.userId = userId;
    }

    public UserNotFound(final UserId userId, final Exception e) {
        super(e);
        this.userId = userId;
    }

    public UserId getUserId() {
        return userId;
    }
}
