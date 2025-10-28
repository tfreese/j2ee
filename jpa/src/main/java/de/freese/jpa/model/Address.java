// Created: 13.03.2010
package de.freese.jpa.model;

import jakarta.persistence.Cacheable;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.ForeignKey;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

/**
 * @author Thomas Freese
 */
@Entity
@Table(name = "T_ADDRESS", schema = "PUBLIC", uniqueConstraints = {@UniqueConstraint(name = "UNQ_ADDRESS_PERSON_STREET", columnNames = {"PERSON_ID", "STREET"})})
@DynamicInsert
@DynamicUpdate
@Cacheable
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE, region = "address")
// @Immutable // All Attributes over Constructor, no Setter.
public class Address extends AbstractEntity {
    public static Address of(final String street) {
        final Address address = new Address();

        address.setStreet(street);

        return address;
    }

    @Id
    @SequenceGenerator(name = "seq_gen_address", sequenceName = "ADDRESS_SEQ", initialValue = 10, allocationSize = 1)
    @GeneratedValue(generator = "seq_gen_address", strategy = GenerationType.SEQUENCE)
    // @BlockSequence(name = "seq_gen_address", blockSize = 10) // Sequence won't be generated and must exist.
    @Column(name = "ID", unique = true, nullable = false)
    private long id = -1;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "PERSON_ID", foreignKey = @ForeignKey(name = "FK_PERSON"), nullable = false, referencedColumnName = "ID")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE, region = "person")
    private Person person;

    @Column(name = "STREET", length = 50, nullable = false)
    // @Convert(converter = StringStripConverter.class)
    private String street;

    public long getID() {
        return id;
    }

    public Person getPerson() {
        return person;
    }

    public String getStreet() {
        return street;
    }

    public void setID(final long id) {
        this.id = id;
    }

    public void setStreet(final String street) {
        this.street = street;
    }

    @Override
    public String toString() {
        final StringBuilder builder = new StringBuilder();
        builder.append("Address [");
        builder.append("id=").append(id);
        builder.append(", street=").append(street);

        if (person != null) {
            builder.append(", person=").append(person.getID());
        }

        builder.append("]");

        return builder.toString();
    }

    /**
     * PreUpdate
     */
    @PrePersist
    void preInsert() {
        System.out.println("Address.preInsert()");
    }

    void setPerson(final Person person) {
        this.person = person;
    }
}
