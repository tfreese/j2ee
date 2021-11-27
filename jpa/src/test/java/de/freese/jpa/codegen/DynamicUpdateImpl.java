// Created: 27.11.2021
package de.freese.jpa.codegen;

import java.lang.annotation.Annotation;

/**
 * @author Thomas Freese
 */
@SuppressWarnings("all")
public class DynamicUpdateImpl implements org.hibernate.annotations.DynamicUpdate
{
    /**
    *
    */
    private final boolean value;

    /**
     * Erstellt ein neues {@link DynamicUpdateImpl} Object.
     */
    public DynamicUpdateImpl()
    {
        this(true);
    }

    /**
     * Erstellt ein neues {@link DynamicUpdateImpl} Object.
     *
     * @param value boolean
     */
    public DynamicUpdateImpl(final boolean value)
    {
        super();

        this.value = value;
    }

    /**
     * @see java.lang.annotation.Annotation#annotationType()
     */
    @Override
    public Class<? extends Annotation> annotationType()
    {
        return org.hibernate.annotations.DynamicUpdate.class;
    }

    /**
     * @see org.hibernate.annotations.DynamicUpdate#value()
     */
    @Override
    public boolean value()
    {
        return this.value;
    }
}
