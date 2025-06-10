package fr.mternez.echopulse.core.common.domain.error;

public class PersistenceException extends Exception {


    public PersistenceException(
            final Class<?> clazz,
            final Throwable cause
    ) {
        super("Repository exception for '%s'.".formatted(clazz), cause);
    }
}
