// Created: 27.11.2021
package de.freese.jpa.codegen;

import java.lang.annotation.Annotation;

/**
 * @author Thomas Freese
 */
@SuppressWarnings("all")
public class ColumnImpl implements javax.persistence.Column
{
    /**
    *
    */
    private final String name;
    /**
    *
    */
    private final boolean nullable;

    /**
     * Erstellt ein neues {@link ColumnImpl} Object.
     *
     * @param name String
     * @param nullable boolean
     */
    public ColumnImpl(final String name, final boolean nullable)
    {
        super();

        this.name = name;
        this.nullable = nullable;
    }

    /**
     * @see java.lang.annotation.Annotation#annotationType()
     */
    @Override
    public Class<? extends Annotation> annotationType()
    {
        return javax.persistence.Column.class;
    }

    /**
     * @see javax.persistence.Column#columnDefinition()
     */
    @Override
    public String columnDefinition()
    {
        return null;
    }

    /**
     * @see javax.persistence.Column#insertable()
     */
    @Override
    public boolean insertable()
    {
        return true;
    }

    /**
     * Default: siehe javax.persistence.Column.length()
     *
     * @see javax.persistence.Column#length()
     */
    @Override
    public int length()
    {
        return 255;
    }

    /**
     * @see javax.persistence.Column#name()
     */
    @Override
    public String name()
    {
        return this.name;
    }

    /**
     * @see javax.persistence.Column#nullable()
     */
    @Override
    public boolean nullable()
    {
        return this.nullable;
    }

    /**
     * @see javax.persistence.Column#precision()
     */
    @Override
    public int precision()
    {
        return 0;
    }

    /**
     * @see javax.persistence.Column#scale()
     */
    @Override
    public int scale()
    {
        return 0;
    }

    /**
     * @see javax.persistence.Column#table()
     */
    @Override
    public String table()
    {
        return null;
    }

    /**
     * @see javax.persistence.Column#unique()
     */
    @Override
    public boolean unique()
    {
        return false;
    }

    /**
     * @see javax.persistence.Column#updatable()
     */
    @Override
    public boolean updatable()
    {
        return true;
    }
}
