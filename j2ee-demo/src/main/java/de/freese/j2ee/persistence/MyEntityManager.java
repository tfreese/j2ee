// Created: 21.05.2013
package de.freese.j2ee.persistence;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import javax.inject.Qualifier;

/**
 * @author Thomas Freese
 */
@Qualifier
@Retention(RetentionPolicy.RUNTIME)
@Target(
{
        ElementType.TYPE, ElementType.METHOD, ElementType.FIELD, ElementType.PARAMETER
})
public @interface MyEntityManager
{
}
