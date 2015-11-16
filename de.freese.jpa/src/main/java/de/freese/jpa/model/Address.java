// Created: 13.03.2010
/**
 * 13.03.2010
 */
package de.freese.jpa.model;

import java.io.Serializable;
import javax.persistence.Cacheable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * @author Thomas Freese
 */
@Cacheable
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE, region = "hibernate.test")
@Entity
@Table(name = "ADDRESS")
public class Address implements Serializable
{
    /**
     *
     */
    private static final long serialVersionUID = 2678405627217507543L;

    /**
     *
     */
    @Id
    @Column(name = "ADDRESS_PK", unique = true, nullable = false)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq")
    @SequenceGenerator(name = "seq", sequenceName = "OBJECT_SEQ", initialValue = 1, allocationSize = 10)
    private Long oid = null;

    /**
     *
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "PERSON_FK")
    private Person person = null;

    /**
     *
     */
    @Column(name = "STREET", nullable = false)
    private String street = null;

    /**
     * Erstellt ein neues {@link Address} Object.
     */
    public Address()
    {
        super();
    }

    /**
     * @return Long
     */
    public Long getOID()
    {
        return this.oid;
    }

    /**
     * @return {@link Person}
     */
    public Person getPerson()
    {
        return this.person;
    }

    /**
     * @return String
     */

    public String getStreet()
    {
        return this.street;
    }

    /**
     * @param oid Long
     */
    public void setOID(final Long oid)
    {
        this.oid = oid;
    }

    /**
     * @param person {@link Person}
     */
    void setPerson(final Person person)
    {
        this.person = person;
    }

    /**
     * @param street String
     */
    public void setStreet(final String street)
    {
        this.street = street;
    }

    /**
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString()
    {
        return getOID() + ": " + getStreet();
    }
}
