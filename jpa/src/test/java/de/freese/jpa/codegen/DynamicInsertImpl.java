// Created: 27.11.2021
package de.freese.jpa.codegen;

import java.lang.annotation.Annotation;

/**
 * @author Thomas Freese
 */
@SuppressWarnings("all")
public class DynamicInsertImpl implements org.hibernate.annotations.DynamicInsert
{
    /**
     *
     */
    private final boolean value;

    /**
     * Erstellt ein neues {@link DynamicInsertImpl} Object.
     */
    public DynamicInsertImpl()
    {
        this(true);
    }

    /**
     * Erstellt ein neues {@link DynamicInsertImpl} Object.
     *
     * @param value boolean
     */
    public DynamicInsertImpl(final boolean value)
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
        return org.hibernate.annotations.DynamicInsert.class;
    }

    /**
     * @see org.hibernate.annotations.DynamicInsert#value()
     */
    @Override
    public boolean value()
    {
        return this.value;
    }
}
