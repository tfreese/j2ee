// Created: 13.03.2010
package de.freese.jpa.model;

import java.io.Serial;
import java.io.Serializable;

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
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.Type;

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
public class Address implements Serializable {
    @Serial
    private static final long serialVersionUID = 2678405627217507543L;

    @Id
    @Column(name = "ID", unique = true, nullable = false)
    //    @SequenceGenerator(name = "seq_gen_address", sequenceName = "ADDRESS_SEQ", allocationSize = 1) // see package-info.java
    @GeneratedValue(generator = "seq_gen_address", strategy = GenerationType.SEQUENCE)
    // @GenericGenerator(name = "my-generator", parameters =
    // {
    // @Parameter(name = "sequenceName", value = "ADDRESS_SEQ"), @Parameter(name = "blockSize", value = "5")
    // }, strategy = "de.freese.jpa.BlockSequenceGenerator")
    // @GeneratedValue(generator = "my-generator")
    private long id = -1;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "PERSON_ID", foreignKey = @ForeignKey(name = "FK_PERSON"), nullable = false, referencedColumnName = "ID")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE, region = "person")
    private Person person;

    @Column(name = "STREET", length = 50, nullable = false)
    @Type(value = StrippedStringType.class)
    private String street;

    public Address() {
        super();
    }

    public Address(final String street) {
        super();

        setStreet(street);
    }

    public long getID() {
        return this.id;
    }

    public Person getPerson() {
        return this.person;
    }

    public String getStreet() {
        return this.street;
    }

    public void setID(final long id) {
        this.id = id;
    }

    public void setStreet(final String street) {
        this.street = street;
    }

    /**
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("Address [");
        builder.append("id=").append(this.id);
        builder.append(", street=").append(this.street);

        if (this.person != null) {
            builder.append(", person=").append(this.person.getID());
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
