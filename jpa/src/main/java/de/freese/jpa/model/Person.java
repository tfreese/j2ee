// Created: 16.08.2006
package de.freese.jpa.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

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
@NamedQuery(name = "allPersons",
        query = "select p from Person p order by p.id asc",
        hints = {@QueryHint(name = "org.hibernate.cacheable", value = "true")})
@NamedQuery(name = "personByVorname",
        query = "select p from Person p where p.vorname = :vorname order by p.name asc",
        hints = {@QueryHint(name = "org.hibernate.cacheable", value = "true")})
@NamedNativeQuery(name = "allPersons.native",
        query = "select p.id, p.name, p.vorname from T_PERSON p order by p.id asc")
// @Immutable // All Attributes over Constructor, no Setter.
public class Person extends AbstractEntity {
    public static Person of(final String name, final String vorname) {
        final Person person = new Person();

        person.setName(name);
        person.setVorname(vorname);

        return person;
    }

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

    // @BlockSequence(name = "seq_gen_person", blockSize = 10) // Sequence won't be generated and must exist.
    // @GeneratedValue(generator = "increment", strategy = GenerationType.AUTO) // select max(id) from
    @Id
    @SequenceGenerator(name = "seq_gen_person", sequenceName = "PERSON_SEQ", allocationSize = 1)
    @GeneratedValue(generator = "seq_gen_person", strategy = GenerationType.SEQUENCE)
    @Column(name = "ID", unique = true, nullable = false)
    @Access(AccessType.FIELD)
    private long id = -1;

    @Column(name = "NAME", columnDefinition = "varchar(255) default 'n.v.'")
    // @Convert(converter = StringStripConverter.class)
    private String name;

    @Column(name = "VORNAME", length = 50, nullable = false)
    // @Convert(converter = StringStripConverter.class)
    private String vorname;

    public void addAddress(final Address address) {
        addresses.add(address);

        address.setPerson(this);
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }

        if (!(o instanceof final Person person)) {
            return false;
        }

        return id == person.id;
    }

    public List<Address> getAddresses() {
        return addresses;
    }

    public long getID() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getVorname() {
        return vorname;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
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

    @Override
    public String toString() {
        final StringBuilder builder = new StringBuilder();
        builder.append("Person [");
        builder.append("id=").append(id);
        builder.append(", name=").append(name);
        builder.append(", vorname=").append(vorname);
        builder.append(", addresses=").append(addresses);
        builder.append("]");

        return builder.toString();
    }

    @PrePersist
    void preInsert() {
        if (cool == null) {
            cool = false;
        }
    }
}
