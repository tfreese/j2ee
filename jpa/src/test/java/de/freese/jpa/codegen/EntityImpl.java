// Created: 27.11.2021
package de.freese.jpa.codegen;

import java.lang.annotation.Annotation;

/**
 * @author Thomas Freese
 */
@SuppressWarnings("all")
public class EntityImpl implements javax.persistence.Entity
{
    /**
     *
     */
    private final String name;

    /**
     * Erstellt ein neues {@link EntityImpl} Object.
     */
    public EntityImpl()
    {
        this(null);
    }

    /**
     * Erstellt ein neues {@link EntityImpl} Object.
     *
     * @param name String
     */
    public EntityImpl(final String name)
    {
        super();

        this.name = name;
    }

    /**
     * @see java.lang.annotation.Annotation#annotationType()
     */
    @Override
    public Class<? extends Annotation> annotationType()
    {
        return javax.persistence.Entity.class;
    }

    /**
     * @see javax.persistence.Entity#name()
     */
    @Override
    public String name()
    {
        return this.name;
    }
}
