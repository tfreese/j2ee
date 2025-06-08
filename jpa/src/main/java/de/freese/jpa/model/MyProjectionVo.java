// Created: 2020-03-22
package de.freese.jpa.model;

import java.io.Serial;
import java.io.Serializable;
import java.util.Objects;

/**
 * @author Thomas Freese
 */
public class MyProjectionVo implements Serializable {
    @Serial
    private static final long serialVersionUID = 8195470174423798274L;

    private final Long id;
    private final String name;

    public MyProjectionVo(final Long id, final String name) {
        super();

        this.id = id;
        this.name = name;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }

        if (!(o instanceof MyProjectionVo dto)) {
            return false;
        }

        return Objects.equals(getId(), dto.getId()) && Objects.equals(getName(), dto.getName());
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getName());
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder(getClass().getSimpleName());
        sb.append("[");
        sb.append("id = ").append(id);
        sb.append(",name = ").append(name);
        sb.append("]");

        return sb.toString();
    }
}
