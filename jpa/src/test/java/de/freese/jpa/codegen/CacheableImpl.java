// Created: 27.11.2021
package de.freese.jpa.codegen;

import java.lang.annotation.Annotation;

/**
 * @author Thomas Freese
 */
@SuppressWarnings("all")
public class CacheableImpl implements javax.persistence.Cacheable
{
    /**
    *
    */
    private final boolean value;

    /**
     * Erstellt ein neues {@link CacheableImpl} Object.
     */
    public CacheableImpl()
    {
        this(true);
    }

    /**
     * Erstellt ein neues {@link CacheableImpl} Object.
     *
     * @param value boolean
     */
    public CacheableImpl(final boolean value)
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
        return javax.persistence.Cacheable.class;
    }

    /**
     * @see javax.persistence.Cacheable#value()
     */
    @Override
    public boolean value()
    {
        return this.value;
    }
}
