/**
 * Created: 10.05.2013
 */
package de.freese.j2ee.model;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

/**
 * @author Thomas Freese
 */
@Entity
@Table(name = "KUNDE", uniqueConstraints = @UniqueConstraint(columnNames =
{
        "NAME", "VORNAME"
}))
// @Cacheable
// @Cache(usage = CacheConcurrencyStrategy.READ_WRITE, region = "hibernate.test")
@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(propOrder =
{
        "name", "vorname"
})
public class Kunde implements Serializable
{
    /**
     *
     */
    private static final long serialVersionUID = 8686116858992640271L;
    /**
     *
     */
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq")
    @SequenceGenerator(name = "seq", sequenceName = "kunde_SEQ", initialValue = 1, allocationSize = 1)
    @XmlAttribute
    private long id;
    /**
     *
     */
    @Column(name = "NAME", nullable = false)
    @Size(min = 3)
    private String name;
    /**
     *
     */
    @Column(name = "VORNAME", nullable = false)
    @Size(min = 3)
    private String vorname;

    /**
     * @return long
     */
    public long getId()
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
     * @return the vorname
     */
    public String getVorname()
    {
        return this.vorname;
    }

    /**
     * @param id the id to set
     */
    public void setId(final long id)
    {
        this.id = id;
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
        return "Kunde [id=" + this.id + ", name=" + this.name + ", vorname=" + this.vorname + "]";
    }
}
