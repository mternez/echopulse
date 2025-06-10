package fr.mternez.echopulse.server.resource.authentication;

import org.springframework.security.core.annotation.AuthenticationPrincipal;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.PARAMETER, ElementType.ANNOTATION_TYPE})
@Retention(RetentionPolicy.RUNTIME)
@AuthenticationPrincipal(expression = "@userResolver.resolve(#this)")
public @interface AuthenticatedUser {
}
