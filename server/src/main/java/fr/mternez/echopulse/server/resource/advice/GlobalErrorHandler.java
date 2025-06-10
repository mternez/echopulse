package fr.mternez.echopulse.server.resource.advice;

import fr.mternez.echopulse.core.common.domain.error.PermissionDenied;
import fr.mternez.echopulse.core.common.domain.error.PersistenceException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalErrorHandler {

    @ExceptionHandler(exception = PermissionDenied.class)
    public String handlePermissionDenied(PermissionDenied ex) throws PersistenceException {
        return ex.getReason();
    }
}
