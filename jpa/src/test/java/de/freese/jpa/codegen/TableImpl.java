// Created: 27.11.2021
package de.freese.jpa.codegen;

import java.lang.annotation.Annotation;

import javax.persistence.Index;
import javax.persistence.UniqueConstraint;

/**
 * @author Thomas Freese
 */
@SuppressWarnings("all")
public class TableImpl implements javax.persistence.Table
{
    /**
     *
     */
    private final String name;
    /**
     *
     */
    private final String schema;

    /**
     * Erstellt ein neues {@link TableImpl} Object.
     *
     * @param schema String
     * @param name String
     */
    public TableImpl(final String schema, final String name)
    {
        super();

        this.schema = schema;
        this.name = name;
    }

    /**
     * @see java.lang.annotation.Annotation#annotationType()
     */
    @Override
    public Class<? extends Annotation> annotationType()
    {
        return javax.persistence.Table.class;
    }

    /**
     * @see javax.persistence.Table#catalog()
     */
    @Override
    public String catalog()
    {
        return null;
    }

    /**
     * @see javax.persistence.Table#indexes()
     */
    @Override
    public Index[] indexes()
    {
        return null;
    }

    /**
     * @see javax.persistence.Table#name()
     */
    @Override
    public String name()
    {
        return this.name;
    }

    /**
     * @see javax.persistence.Table#schema()
     */
    @Override
    public String schema()
    {
        return this.schema;
    }

    /**
     * @see javax.persistence.Table#uniqueConstraints()
     */
    @Override
    public UniqueConstraint[] uniqueConstraints()
    {
        return null;
    }
}
