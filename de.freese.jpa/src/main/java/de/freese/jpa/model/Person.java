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
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.QueryHint;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

/**
 * @author Thomas Freese
 */
@Entity
@Table(name = "T_PERSON", uniqueConstraints =
{
    @UniqueConstraint(name = "UNQ_NAME_VORNAME", columnNames =
    {
            "NAME", "VORNAME"
    })
})
@Cacheable
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE, region = "person")
@NamedQueries(
        {
            @NamedQuery(name = "allPersons", query = "from Person order by id asc", hints =
                {
                    @QueryHint(name = "org.hibernate.cacheable", value = "true")
                }), @NamedQuery(name = "personByVorname", query = "from Person where vorname=:vorname order by name asc", hints =
            {
                        @QueryHint(name = "org.hibernate.cacheable", value = "true")
            })
        })
public class Person implements Serializable
{
    /**
     *
     */
    private static final long serialVersionUID = 413810580854319964L;

    /**
     *
     */
    @OneToMany(mappedBy = "person", fetch = FetchType.LAZY, cascade =
    {
            CascadeType.PERSIST, CascadeType.REMOVE
    })
    @OrderBy("street desc")
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE, region = "collections")
    @Fetch(FetchMode.SELECT)
    private List<Address> addresses = new ArrayList<>();

    /**
     * SequenceGeneratoren wiederzuverwenden über orm.xml
     */
    @Id
    @Column(name = "ID", unique = true, nullable = false)
    // @SequenceGenerator(name = "seq_gen_person", sequenceName = "OBJECT_SEQ", initialValue = 10, allocationSize = 10)
    // @GeneratedValue(generator = "seq_gen_person", strategy = GenerationType.SEQUENCE)
    @GeneratedValue(generator = "seq_gen", strategy = GenerationType.SEQUENCE)
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
    private String vorName = null;

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

        this.name = name;
        this.vorName = vorname;
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
    public String getVorName()
    {
        return this.vorName;
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
     * @param vorName String
     */
    public void setVorName(final String vorName)
    {
        this.vorName = vorName;
    }

    /**
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString()
    {
        return getID() + ": " + getName() + ", " + getVorName();
    }
}
