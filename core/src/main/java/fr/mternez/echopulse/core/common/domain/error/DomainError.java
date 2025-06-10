package fr.mternez.echopulse.core.common.domain.error;

public class DomainError extends RuntimeException {

    public DomainError() {
        super();
    }

    public DomainError(final Throwable cause) {
        super(cause);
    }
}
