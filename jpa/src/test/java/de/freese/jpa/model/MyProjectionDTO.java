// Created: 2020-03-22
package de.freese.jpa.model;

import java.io.Serializable;
import java.util.Objects;

/**
 * @author Thomas Freese
 */
public class MyProjectionDTO implements Serializable
{
    /**
     *
     */
    private static final long serialVersionUID = 8195470174423798274L;
    /**
     *
     */
    private final Long id;
    /**
     *
     */
    private final String name;

    /**
     * Erstellt ein neues {@link MyProjectionDTO} Object.
     *
     * @param id Long
     * @param name String
     */
    public MyProjectionDTO(final Long id, final String name)
    {
        super();

        this.id = id;
        this.name = name;
    }

    /**
     * @see java.lang.Object#equals(java.lang.Object)
     */
    @Override
    public boolean equals(final Object obj)
    {
        if (this == obj)
        {
            return true;
        }

        if ((obj == null) || !(obj instanceof MyProjectionDTO other))
        {
            return false;
        }

        if (!Objects.equals(this.id, other.id))
        {
            return false;
        }

        if (!Objects.equals(this.name, other.name))
        {
            return false;
        }

        return true;
    }

    /**
     * @return Long
     */
    public Long getId()
    {
        return this.id;
    }

    /**
     * @return String
     */
    public String getName()
    {
        return this.name;
    }

    /**
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode()
    {
        return Objects.hash(id, name);
    }

    /**
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString()
    {
        StringBuilder sb = new StringBuilder();
        sb.append("TPerson [");
        sb.append("id = ").append(this.id);
        sb.append(",name = ").append(this.name);
        sb.append("]");

        return sb.toString();
    }
}
