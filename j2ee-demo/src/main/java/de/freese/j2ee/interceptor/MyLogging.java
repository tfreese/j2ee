// Created: 21.05.2013
package de.freese.j2ee.interceptor;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import jakarta.inject.Qualifier;
import jakarta.interceptor.InterceptorBinding;

/**
 * @author Thomas Freese
 */
@Qualifier
@Retention(RetentionPolicy.RUNTIME)
@Target(
        {
                ElementType.TYPE, ElementType.METHOD
        })
@InterceptorBinding
public @interface MyLogging
{
}
