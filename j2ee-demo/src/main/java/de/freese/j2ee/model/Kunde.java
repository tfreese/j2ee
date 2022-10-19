// Created: 10.05.2013
package de.freese.j2ee.model;

import java.io.Serial;
import java.io.Serializable;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import jakarta.validation.constraints.Size;
import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlAttribute;
import jakarta.xml.bind.annotation.XmlRootElement;
import jakarta.xml.bind.annotation.XmlType;

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
    @Serial
    private static final long serialVersionUID = 8686116858992640271L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq")
    @SequenceGenerator(name = "seq", sequenceName = "kunde_SEQ", initialValue = 1, allocationSize = 1)
    @XmlAttribute
    private long id;

    @Column(name = "NAME", nullable = false)
    @Size(min = 3)
    private String name;

    @Column(name = "VORNAME", nullable = false)
    @Size(min = 3)
    private String vorname;

    public long getId()
    {
        return this.id;
    }

    public String getName()
    {
        return this.name;
    }

    public String getVorname()
    {
        return this.vorname;
    }

    public void setId(final long id)
    {
        this.id = id;
    }

    public void setName(final String name)
    {
        this.name = name;
    }

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
