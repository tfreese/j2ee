package de.freese.querydsl;

import java.io.Serializable;
import javax.annotation.Generated;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import com.querydsl.sql.Column;

/**
 * TPerson ist ein Querydsl bean typ. Table: T_PERSON
 */
@Generated("de.freese.jpa.MyQueryDSLBeanSerializer")
public class TPerson implements Serializable
{
    /**
     *
     */
    private static final long serialVersionUID = -371984183L;

    /**
     *
     */
    @Column("MY_ID")
    @NotNull
    private Long myId;

    /**
     *
     */
    @Column("NAME")
    @Size(max = 25)
    private String name;

    /**
     *
     */
    @Column("VORNAME")
    @Size(max = 25)
    private String vorname;

    /**
     * Default Constructor
     */
    public TPerson()
    {
        super();
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

        if (!(obj instanceof TPerson))
        {
            return false;
        }

        TPerson other = (TPerson) obj;

        if (this.myId == null)
        {
            if (other.myId != null)
            {
                return false;
            }
        }
        else if (!this.myId.equals(other.myId))
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

        if (this.vorname == null)
        {
            if (other.vorname != null)
            {
                return false;
            }
        }
        else if (!this.vorname.equals(other.vorname))
        {
            return false;
        }

        return true;
    }

    /**
     * @return Long
     */
    public Long getMyId()
    {
        return this.myId;
    }

    /**
     * @return String
     */
    public String getName()
    {
        return this.name;
    }

    /**
     * @return String
     */
    public String getVorname()
    {
        return this.vorname;
    }

    /**
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode()
    {
        final int prime = 31;
        int result = 1;
        result = (prime * result) + ((this.myId == null) ? 0 : this.myId.hashCode());
        result = (prime * result) + ((this.name == null) ? 0 : this.name.hashCode());
        result = (prime * result) + ((this.vorname == null) ? 0 : this.vorname.hashCode());

        return result;
    }

    /**
     * @param myId Long
     */
    public void setMyId(final Long myId)
    {
        this.myId = myId;
    }

    /**
     * @param name String
     */
    public void setName(final String name)
    {
        this.name = name;
    }

    /**
     * @param vorname String
     */
    public void setVorname(final String vorname)
    {
        this.vorname = vorname;
    }

    /**
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString()
    {
        StringBuilder sb = new StringBuilder();
        sb.append("TPerson [");
        sb.append("myId = ").append(this.myId);
        sb.append(",name = ").append(this.name);
        sb.append(",vorname = ").append(this.vorname);
        sb.append("]");

        return sb.toString();
    }
}
