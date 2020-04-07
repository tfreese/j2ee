// Created: 2020-03-22 09.57.14,209
package de.freese.jpa.model;

import java.io.Serializable;

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

        if (obj == null)
        {
            return false;
        }

        if (!(obj instanceof MyProjectionDTO))
        {
            return false;
        }

        MyProjectionDTO other = (MyProjectionDTO) obj;

        if (this.id == null)
        {
            if (other.id != null)
            {
                return false;
            }
        }
        else if (!this.id.equals(other.id))
        {
            return false;
        }

        if (this.name == null)
        {
            if (other.name != null)
            {
                return false;
            }
        }
        else if (!this.name.equals(other.name))
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
        final int prime = 31;
        int result = 1;

        result = (prime * result) + ((this.id == null) ? 0 : this.id.hashCode());
        result = (prime * result) + ((this.name == null) ? 0 : this.name.hashCode());

        return result;
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
