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
import javax.persistence.ForeignKey;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
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
public class Address implements Serializable
{
    /**
     *
     */
    private static final long serialVersionUID = 2678405627217507543L;

    /**
     * SequenceGeneratoren wiederzuverwenden über orm.xml
     */
    @Id
    @Column(name = "ID", unique = true, nullable = false)
    @SequenceGenerator(name = "seq_gen_address", sequenceName = "ADDRESS_SEQ", initialValue = 1, allocationSize = 10)
    @GeneratedValue(generator = "seq_gen_address", strategy = GenerationType.SEQUENCE)
    // @GeneratedValue(generator = "seq_gen", strategy = GenerationType.SEQUENCE)
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
    @Column(name = "STREET", length = 50, nullable = false)
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
        StringBuilder builder = new StringBuilder();
        builder.append("Address [id=");
        builder.append(this.id);
        builder.append(", street=");
        builder.append(this.street);
        builder.append(", person=");
        builder.append(this.person.getID());
        builder.append("]");

        return builder.toString();
    }
}
