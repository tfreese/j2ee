package de.freese.jpa.model;

import java.io.Serial;
import java.io.Serializable;
import java.util.Objects;

/**
 * @author Thomas Freese
 * @since 22.03.2020
 */
public record MyProjectionVo(Long id, String name) implements Serializable {
    @Serial
    private static final long serialVersionUID = 8195470174423798274L;

    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }

        if (!(o instanceof MyProjectionVo(final Long id1, final String name1))) {
            return false;
        }

        return Objects.equals(id(), id1) && Objects.equals(name(), name1);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id(), name());
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + "["
                + "id = " + id
                + ",name = " + name
                + "]";
    }
}
