// Created: 13.03.2010
package de.freese.jpa.model;

import java.io.Serializable;

import javax.persistence.Cacheable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ForeignKey;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.PrePersist;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

/**
 * @author Thomas Freese
 */
@Entity
@Table(name = "T_ADDRESS", schema = "PUBLIC", uniqueConstraints =
        {
                @UniqueConstraint(name = "UNQ_ADDRESS_PERSON_STREET", columnNames =
                        {
                                "PERSON_ID", "STREET"
                        })
        })
@DynamicInsert
@DynamicUpdate
@Cacheable
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE, region = "address")
// @Immutable // Alle Attribute nur über Konstruktor, keine Setter.
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
    @Column(name = "ID", unique = true, nullable = false)
    @SequenceGenerator(name = "seq_gen_address", sequenceName = "ADDRESS_SEQ", initialValue = 1, allocationSize = 10)
    @GeneratedValue(generator = "seq_gen_address", strategy = GenerationType.SEQUENCE)
    // @GenericGenerator(name = "my-generator", parameters =
    // {
    // @Parameter(name = "sequenceName", value = "ADDRESS_SEQ"), @Parameter(name = "blockSize", value = "5")
    // }, strategy = "de.freese.jpa.BlockSequenceGenerator")
    // @GeneratedValue(generator = "my-generator")
    private long id = -1;
    /**
     *
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "PERSON_ID", foreignKey = @ForeignKey(name = "FK_PERSON"), nullable = false)
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE, region = "person")
    private Person person = null;
    /**
     *
     */
    @Column(name = "STREET", length = 50, nullable = false, insertable = true, updatable = true)
    private String street = null;

    /**
     * Erstellt ein neues {@link Address} Object.
     */
    public Address()
    {
        super();
    }

    /**
     * Erstellt ein neues {@link Address} Object.
     *
     * @param street String
     */
    public Address(final String street)
    {
        super();

        setStreet(street);
    }

    /**
     * @return long
     */
    public long getID()
    {
        return this.id;
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
     * @param id long
     */
    public void setID(final long id)
    {
        this.id = id;
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
        StringBuilder builder = new StringBuilder();
        builder.append("Address [");
        builder.append("id=").append(this.id);
        builder.append(", street=").append(this.street);
        builder.append(", person=").append(this.person.getID());
        builder.append("]");

        return builder.toString();
    }

    /**
     * @PreUpdate
     */
    @PrePersist
    void preInsert()
    {
        // TODO
        System.out.println("Address.preInsert()");
    }

    /**
     * @param person {@link Person}
     */
    void setPerson(final Person person)
    {
        this.person = person;
    }
}
