package de.freese.spring.embedded.domain;

import javax.validation.constraints.NotNull;
import com.mysema.query.sql.Column;
import javax.validation.constraints.Size;
import javax.annotation.Generated;

/**
 * MyTable is a Querydsl bean type
 */
@Generated("com.mysema.query.sql.codegen.ExtendedBeanSerializer")
public class MyTable {

    @NotNull
    @Column("DATUM")
    private java.sql.Timestamp datum;

    @NotNull
    @Column("ID")
    private Long id;

    @Size(max=250)
    @NotNull
    @Column("NAME")
    private String name;

    public java.sql.Timestamp getDatum() {
        return datum;
    }

    public void setDatum(java.sql.Timestamp datum) {
        this.datum = datum;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public boolean equals(Object o) {
        if (id == null) {
            return super.equals(o);
        }
        if (!(o instanceof MyTable)) {
            return false;
        }
        MyTable obj = (MyTable)o;
        return id.equals(obj.id);
    }

    @Override
    public int hashCode() {
        if (id == null) {
            return super.hashCode();
        }
        final int prime = 31;
        int result = 1;
        result = prime * result + id.hashCode();
        return result;
    }

    @Override
    public String toString() {
        return "MyTable#" + id;
    }

}

