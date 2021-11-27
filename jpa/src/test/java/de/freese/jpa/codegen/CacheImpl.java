// Created: 27.11.2021
package de.freese.jpa.codegen;

import java.lang.annotation.Annotation;

import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * @author Thomas Freese
 */
@SuppressWarnings("all")
public class CacheImpl implements org.hibernate.annotations.Cache
{
    /**
     *
     */
    private final String region;
    /**
     *
     */
    private final CacheConcurrencyStrategy usage;

    /**
     * Erstellt ein neues {@link CacheImpl} Object.
     *
     * @param usage {@link CacheConcurrencyStrategy}
     * @param region String
     */
    public CacheImpl(final CacheConcurrencyStrategy usage, final String region)
    {
        super();

        this.usage = usage;
        this.region = region;
    }

    /**
     * @see java.lang.annotation.Annotation#annotationType()
     */
    @Override
    public Class<? extends Annotation> annotationType()
    {
        return org.hibernate.annotations.Cache.class;
    }

    /**
     * @see org.hibernate.annotations.Cache#include()
     */
    @Override
    public String include()
    {
        return null;
    }

    /**
     * @see org.hibernate.annotations.Cache#region()
     */
    @Override
    public String region()
    {
        return this.region;
    }

    /**
     * @see org.hibernate.annotations.Cache#usage()
     */
    @Override
    public CacheConcurrencyStrategy usage()
    {
        return this.usage;
    }
}
