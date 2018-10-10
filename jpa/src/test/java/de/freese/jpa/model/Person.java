/**
 * 16.08.2006
 */
package de.freese.jpa.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Cacheable;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedNativeQuery;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.PrePersist;
import javax.persistence.QueryHint;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

/**
 * @author Thomas Freese
 */
@Entity
@Table(name = "T_PERSON", schema = "PUBLIC", uniqueConstraints =
{
        @UniqueConstraint(name = "UNQ_PERSON_NAME_VORNAME", columnNames =
        {
                "NAME", "VORNAME"
        })
})
@DynamicInsert
@DynamicUpdate
@Cacheable
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE, region = "person")
@NamedQueries(
{
        @NamedQuery(name = "allPersons", query = "select p from Person p order by p.id asc", hints =
        {
                @QueryHint(name = "org.hibernate.cacheable", value = "true")
        }), @NamedQuery(name = "personByVorname", query = "select p from Person p where p.vorname = :vorname order by p.name asc", hints =
        {
                @QueryHint(name = "org.hibernate.cacheable", value = "true")
        })
})
@NamedNativeQuery(name = "allPersons.native", query = "select p.id, p.name, p.vorname from T_PERSON p order by p.id asc")
public class Person implements Serializable
{
    /**
     *
     */
    private static final long serialVersionUID = 413810580854319964L;

    /**
     * orphanRemoval = true
     */
    @OneToMany(mappedBy = "person", fetch = FetchType.LAZY, orphanRemoval = true, cascade =
    {
            CascadeType.ALL
    })
    @OrderBy("street desc")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE, region = "collections")
    @Fetch(FetchMode.SELECT)
    private List<Address> addresses = new ArrayList<>();

    /**
     * , columnDefinition="Decimal(10,2) default '100.00'"
     */
    @Column(name = "COOL", nullable = true, insertable = true, updatable = true, precision = 1, scale = 0)
    @ColumnDefault("false")
    private Boolean cool = null;

    /**
     * SequenceGeneratoren wiederzuverwenden über orm.xml
     */
    @Id
    @Column(name = "ID", unique = true, nullable = false)
    @SequenceGenerator(name = "seq_gen_person", sequenceName = "PERSON_SEQ", initialValue = 1, allocationSize = 10)
    @GeneratedValue(generator = "seq_gen_person", strategy = GenerationType.SEQUENCE)
    // @GenericGenerator(name = "my-generator", parameters =
    // {
    // @Parameter(name = "sequenceName", value = "PERSON_SEQ"), @Parameter(name = "blockSize", value = "5")
    // }, strategy = "de.freese.jpa.BlockSequenceGenerator")
    // @GeneratedValue(generator = "my-generator")
    @Access(AccessType.FIELD)
    private long id = -1;

    /**
     *
     */
    @Column(name = "NAME", length = 50, nullable = false)
    private String name = null;

    /**
     *
     */
    @Column(name = "VORNAME", length = 50, nullable = false)
    private String vorname = null;

    /**
     * Creates a new {@link Person} object.
     */
    public Person()
    {
        super();
    }

    /**
     * Creates a new {@link Person} object.
     *
     * @param name String
     * @param vorname String
     */
    public Person(final String name, final String vorname)
    {
        super();

        setName(name);
        setVorname(vorname);
    }

    /**
     * @param address {@link Address}
     */
    public void addAddress(final Address address)
    {
        this.addresses.add(address);
        address.setPerson(this);
    }

    /**
     * @return List<Address>
     */

    public List<Address> getAddresses()
    {
        return this.addresses;
    }

    /**
     * @return long
     */
    public long getID()
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
        return Long.valueOf(getID()).hashCode();
    }

    /**
     * @PreUpdate
     */
    @PrePersist
    void preInsert()
    {
        if (this.cool == null)
        {
            this.cool = false;
        }
    }

    /**
     * @param id long
     */
    public void setID(final long id)
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
        StringBuilder builder = new StringBuilder();
        builder.append("Person [id=");
        builder.append(this.id);
        builder.append(", name=");
        builder.append(this.name);
        builder.append(", vorname=");
        builder.append(this.vorname);
        builder.append(", addresses=");
        builder.append(this.addresses);
        builder.append("]");

        return builder.toString();
    }
}
