// Created: 16.08.2006
package de.freese.jpa.model;

import java.io.Serial;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.Access;
import jakarta.persistence.AccessType;
import jakarta.persistence.Cacheable;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.NamedNativeQuery;
import jakarta.persistence.NamedQueries;
import jakarta.persistence.NamedQuery;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OrderBy;
import jakarta.persistence.PrePersist;
import jakarta.persistence.QueryHint;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;

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
@Table(name = "T_PERSON", schema = "PUBLIC", uniqueConstraints = {@UniqueConstraint(name = "UNQ_PERSON_NAME_VORNAME", columnNames = {"NAME", "VORNAME"})})
@DynamicInsert
@DynamicUpdate
@Cacheable
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE, region = "person")
@NamedQueries({@NamedQuery(name = "allPersons", query = "select p from Person p order by p.id asc", hints = {@QueryHint(name = "org.hibernate.cacheable", value = "true")}), @NamedQuery(name = "personByVorname", query = "select p from Person p where p.vorname = :vorname order by p.name asc", hints = {@QueryHint(name = "org.hibernate.cacheable", value = "true")})})
@NamedNativeQuery(name = "allPersons.native", query = "select p.id, p.name, p.vorname from T_PERSON p order by p.id asc")
// @Immutable // Alle Attribute nur über Konstruktor, keine Setter.
public class Person implements Serializable {
    @Serial
    private static final long serialVersionUID = 413810580854319964L;

    /**
     * orphanRemoval = true
     */
    @OneToMany(mappedBy = "person", fetch = FetchType.LAZY, orphanRemoval = true, cascade = {CascadeType.ALL})
    @OrderBy("street desc")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE, region = "collections")
    @Fetch(FetchMode.SELECT)
    private final List<Address> addresses = new ArrayList<>();

    /**
     * , columnDefinition="Decimal(10,2) default '100.00'"
     */
    @Column(name = "COOL", precision = 1)
    @ColumnDefault("false")
    private Boolean cool;

    @Id
    @Column(name = "ID", unique = true, nullable = false)
    @SequenceGenerator(name = "seq_gen_person", sequenceName = "PERSON_SEQ", allocationSize = 10)
    @GeneratedValue(generator = "seq_gen_person", strategy = GenerationType.SEQUENCE)
    // @GenericGenerator(name = "my-generator", parameters =
    // {
    // @Parameter(name = "sequenceName", value = "PERSON_SEQ"), @Parameter(name = "blockSize", value = "5")
    // }, strategy = "de.freese.jpa.BlockSequenceGenerator")
    // @GeneratedValue(generator = "my-generator")
    @Access(AccessType.FIELD)
    private long id = -1;

    @Column(name = "NAME", length = 50, nullable = false)
    private String name;

    @Column(name = "VORNAME", length = 50, nullable = false)
    private String vorname;

    public Person() {
        super();
    }

    public Person(final String name, final String vorname) {
        super();

        setName(name);
        setVorname(vorname);
    }

    public void addAddress(final Address address) {
        this.addresses.add(address);
        address.setPerson(this);
    }

    public List<Address> getAddresses() {
        return this.addresses;
    }

    public long getID() {
        return this.id;
    }

    public String getName() {
        return this.name;
    }

    public String getVorname() {
        return this.vorname;
    }

    /**
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode() {
        return Long.valueOf(getID()).hashCode();
    }

    public void setID(final long id) {
        this.id = id;
    }

    public void setName(final String name) {
        this.name = name;
    }

    public void setVorname(final String vorname) {
        this.vorname = vorname;
    }

    /**
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("Person [");
        builder.append("id=").append(this.id);
        builder.append(", name=").append(this.name);
        builder.append(", vorname=").append(this.vorname);
        builder.append(", addresses=").append(this.addresses);
        builder.append("]");

        return builder.toString();
    }

    @PrePersist
    void preInsert() {
        if (this.cool == null) {
            this.cool = false;
        }
    }
}
