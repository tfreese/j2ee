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
        return this.id;
    }

    public String getName() {
        return this.name;
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getName());
    }

    /**
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder(getClass().getSimpleName());
        sb.append("[");
        sb.append("id = ").append(this.id);
        sb.append(",name = ").append(this.name);
        sb.append("]");

        return sb.toString();
    }
}
