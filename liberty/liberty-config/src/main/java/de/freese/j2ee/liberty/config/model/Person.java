// Created: 27 Juli 2024
package de.freese.j2ee.liberty.config.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;

/**
 * @author Thomas Freese
 */
@Entity
@Table(name = "T_PERSON")
public class Person {
    @Id
    @SequenceGenerator(name = "seq_gen_person", sequenceName = "PERSON_SEQ", initialValue = 1, allocationSize = 1)
    @GeneratedValue(generator = "seq_gen_person", strategy = GenerationType.SEQUENCE)
    @Column(name = "ID", unique = true, nullable = false)
    private long id = -1;

    @Column(name = "NAME", nullable = false)
    private String name;

    public long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setId(final long id) {
        this.id = id;
    }

    public void setName(final String name) {
        this.name = name;
    }
}
