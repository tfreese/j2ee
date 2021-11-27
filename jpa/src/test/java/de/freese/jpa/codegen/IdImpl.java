// Created: 27.11.2021
package de.freese.jpa.codegen;

import java.lang.annotation.Annotation;

/**
 * @author Thomas Freese
 */
@SuppressWarnings("all")
public class IdImpl implements javax.persistence.Id
{
    /**
     * @see java.lang.annotation.Annotation#annotationType()
     */
    @Override
    public Class<? extends Annotation> annotationType()
    {
        return javax.persistence.Id.class;
    }
}
